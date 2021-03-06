package com.github.vatbub.safeAPIKeyStore.server;

/*-
 * #%L
 * com.github.vatbub.server
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.ListCommon;
import com.github.vatbub.common.core.SystemUtils;
import com.github.vatbub.common.core.logging.FOKLogger;
import com.github.vatbub.safeAPIKeyStore.common.*;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * The API key server.
 */
public class Server {
    private static final String FILE_NAME_FOR_USED_PUBLIC_KEYS = "usedPublicKeys";

    private Properties apiKeys;
    private com.esotericsoftware.kryonet.Server kryoServer;
    private ArrayList<byte[]> usedKeys;
    private boolean autoShutdownEnabled;
    private long minutesToIdleBeforeAutoShutdown;

    /**
     * Launches a server on the specified port with the specified api keys.
     *
     * @param port            The port to run the server on
     * @param apiKeysFileName The absolute or relative file path to the properties-file that contains the api keys to serve
     * @throws IOException If the server fails to launch for any reason.
     */
    public Server(int port, String apiKeysFileName) throws IOException {
        this(port, new File(apiKeysFileName));
    }

    /**
     * Launches a server on the specified port with the specified api keys.
     *
     * @param port        The port to run the server on
     * @param apiKeysFile The properties-file that contains the api keys to serve
     * @throws IOException If the server fails to launch for any reason.
     */
    public Server(int port, File apiKeysFile) throws IOException {
        // init api keys
        FOKLogger.info(Server.class.getName(), "Resolving the apiKeysFile to '" + apiKeysFile.getAbsolutePath() + "'");
        Properties tempApiKeys = new Properties();
        tempApiKeys.load(new FileReader(apiKeysFile));
        apiKeys = tempApiKeys;

        readUsedPublicKeys();

        initServer(port);
    }

    /**
     * Returns the currently served api keys
     *
     * @return The currently served api keys
     */
    public Properties getApiKeys() {
        return apiKeys;
    }

    private void initServer(int port) throws IOException {
        kryoServer = new com.esotericsoftware.kryonet.Server();
        KryoCommon.registerClasses(kryoServer.getKryo());
        kryoServer.bind(port);

        kryoServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                SystemUtils.getInstance().cancelAutoShutdownTimer();
                Object response = createResponse(object);
                if (response != null) {
                    connection.sendTCP(response);
                }
                setAutoShutdownTimer();
            }
        });

        kryoServer.start();
    }

    /**
     * Called when the server receives an object.
     * Override this method if you wish to modify the server's default response.
     *
     * @param receivedObject The object received by the server
     * @return The object to send as a response. Set this to {@code null} to send no response at all
     */
    public Object createResponse(Object receivedObject) {
        try {
            if (receivedObject instanceof APIKeyRequest) {
                APIKeyRequest request = (APIKeyRequest) receivedObject;
                byte[] encodedPublicKey = request.getEncodedClientPublicKey();
                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedPublicKey));
                if (ListCommon.listContainsArray(usedKeys, encodedPublicKey)) {
                    return new MultipleRequestsWithSameRSAKeyExceptionInternalImpl();
                } else {
                    addUsedPublicKey(encodedPublicKey);

                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                    Charset encoding = Charset.forName("UTF-8");
                    return new APIKeyResponse(request.getRequestedApiKeyName(), cipher.doFinal(apiKeys.getProperty(request.getRequestedApiKeyName()).getBytes(encoding)), encoding.name());
                }
            } else {
                return new BadRequestExceptionInternalImpl("Unknown object type");
            }
        } catch (Exception e) {
            FOKLogger.log(Server.class.getName(), Level.SEVERE, "Internal server exception", e);
            return new InternalServerExceptionInternalImpl(e.getMessage(), ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void addUsedPublicKey(byte[] encodedPublicKey) {
        usedKeys.add(encodedPublicKey);
        try {
            saveUsedPublicKeys();
        } catch (FileNotFoundException e) {
            FOKLogger.log(Server.class.getName(), Level.SEVERE, "Could not save the used keys list", e);
        }
    }

    /**
     * Causes the server to re-read the already used (and thus blocked) public RSA keys from disk.
     * Reads the list from the default location ({@code <appDataPath>/com.github.vatbub.safeAPIKeyStore.server/usedPublicKeys})
     */
    public void readUsedPublicKeys() {
        readUsedPublicKeys(Common.getInstance().getAndCreateAppDataPath() + FILE_NAME_FOR_USED_PUBLIC_KEYS);
    }

    /**
     * Causes the server to re-read the already used (and thus blocked) public RSA keys from disk.
     *
     * @param filePath The absolute or relative path to the file to read the used keys list from
     */
    @SuppressWarnings("unchecked")
    public void readUsedPublicKeys(String filePath) {
        FOKLogger.info(Server.class.getName(), "Trying to read the used public keys list from '" + filePath + "'...");
        try (Input input = new Input((new FileInputStream(filePath)))) {
            usedKeys = (ArrayList<byte[]>) getKryoToSaveUsedPublicKeys().readClassAndObject(input);
        } catch (KryoException | ClassCastException | NullPointerException | FileNotFoundException e) {
            FOKLogger.severe(Server.class.getName(), "Unable to read the used public keys list from disk (" + e.getClass().getName() + "), initializing an empty list...");
            File usedKeysFile = new File(filePath);
            if (usedKeysFile.exists()) {
                try {
                    Files.delete(usedKeysFile.toPath());
                } catch (IOException e1) {
                    FOKLogger.severe(Server.class.getName(), "Unable to delete the old used public key list from the drive (" + e.getClass().getName() + "), the server will try to overwrite the file later...");
                }
            }
            usedKeys = new ArrayList<>();
        }
    }

    /**
     * Saves the list of used (and thus blocked) public RSA keys to the default location ({@code <appDataPath>/com.github.vatbub.safeAPIKeyStore.server/usedPublicKeys})
     *
     * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void saveUsedPublicKeys() throws FileNotFoundException {
        saveUsedPublicKeys(Common.getInstance().getAndCreateAppDataPath() + FILE_NAME_FOR_USED_PUBLIC_KEYS);
    }

    /**
     * Saves the list of used (and thus blocked) public RSA keys to the specified location
     *
     * @param filePath The absolute or relative path to the file to save the list in
     * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void saveUsedPublicKeys(String filePath) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(filePath));
        getKryoToSaveUsedPublicKeys().writeClassAndObject(output, usedKeys);
        output.close();
    }

    private Kryo getKryoToSaveUsedPublicKeys() {
        Kryo kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(byte[].class);
        kryo.setReferences(true);
        return kryo;
    }

    /**
     * Resets the list of used public RSA keys permanently by deleting the corresponding file on the drive.
     * If the file cannot be deleted for any reason, a temporary reset is performed (server will be reset until rebooted)
     * This method assumes that the list was saved on the default location ({@code <appDataPath>/com.github.vatbub.safeAPIKeyStore.server/usedPublicKeys}).
     *
     * @return {@code true} if the server was reset permanently, {@code false} if only temporarily
     * @see #resetTemporarily()
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean resetPermanently() {
        return resetPermanently(Common.getInstance().getAndCreateAppDataPath() + FILE_NAME_FOR_USED_PUBLIC_KEYS);
    }

    /**
     * Resets the list of used public RSA keys permanently by deleting the corresponding file on the drive.
     * If the file cannot be deleted for any reason, a temporary reset is performed (server will be reset until rebooted)
     *
     * @param pathToUsedKeysFile The absolute or relative path to the file that contains the list of used public RSA keys
     * @return {@code true} if the server was reset permanently, {@code false} if only temporarily
     * @see #resetTemporarily()
     */
    public boolean resetPermanently(String pathToUsedKeysFile) {
        SystemUtils.getInstance().cancelAutoShutdownTimer();
        File usedKeysFile = new File(pathToUsedKeysFile);
        if (usedKeysFile.exists()) {
            try {
                Files.delete(usedKeysFile.toPath());
            } catch (IOException e) {
                FOKLogger.log(Server.class.getName(), Level.SEVERE, "Failed to delete the file '" + pathToUsedKeysFile + "', server will reset temporarily only.", e);
                resetTemporarily();
                return false;
            }
        }

        readUsedPublicKeys(pathToUsedKeysFile);
        return true;
    }

    /**
     * Resets the list of public RSA keys temporarily (until the server is rebooted)
     */
    public void resetTemporarily() {
        usedKeys = new ArrayList<>();
        SystemUtils.getInstance().cancelAutoShutdownTimer();
    }

    /**
     * Shuts the server down.
     */
    public void stop() {
        kryoServer.stop();
    }

    public boolean isAutoShutdownEnabled() {
        return autoShutdownEnabled;
    }

    public void setAutoShutdownEnabled(boolean autoShutdownEnabled) {
        this.autoShutdownEnabled = autoShutdownEnabled;
        setAutoShutdownTimer();
    }

    public long getMinutesToIdleBeforeAutoShutdown() {
        return minutesToIdleBeforeAutoShutdown;
    }

    public void setMinutesToIdleBeforeAutoShutdown(long minutesToIdleBeforeAutoShutdown) {
        this.minutesToIdleBeforeAutoShutdown = minutesToIdleBeforeAutoShutdown;
        setAutoShutdownTimer();
    }

    private void setAutoShutdownTimer() {
        if (isAutoShutdownEnabled())
            SystemUtils.getInstance().startAutoShutdownTimer(getMinutesToIdleBeforeAutoShutdown(), TimeUnit.MINUTES);
    }
}
