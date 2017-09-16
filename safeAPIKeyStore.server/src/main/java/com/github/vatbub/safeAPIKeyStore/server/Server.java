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
import com.github.vatbub.common.core.logging.FOKLogger;
import com.github.vatbub.safeAPIKeyStore.common.*;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

public class Server {
    private final String fileNameForUsedPublicKeys = "usedPublicKeys";

    private Properties apiKeys;
    private com.esotericsoftware.kryonet.Server kryoServer;
    private ArrayList<byte[]> usedKeys;

    public Server(int port, String apiKeysFileName) throws IOException {
        this(port, new File(apiKeysFileName));
    }

    public Server(int port, File apiKeysFile) throws IOException {
        // init api keys
        FOKLogger.info(Server.class.getName(), "Resolving the apiKeysFile to '" + apiKeysFile.getAbsolutePath() + "'");
        Properties tempApiKeys = new Properties();
        tempApiKeys.load(new FileReader(apiKeysFile));
        apiKeys = tempApiKeys;

        readUsedPublicKeys();

        initServer(port);
    }

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
                try {
                    if (object instanceof APIKeyRequest) {
                        APIKeyRequest request = (APIKeyRequest) object;
                        byte[] encodedPublicKey = request.getEncodedClientPublicKey();
                        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedPublicKey));
                        if (usedKeys.contains(encodedPublicKey)) {
                            connection.sendTCP(new MultipleRequestsWithSameRSAKeyException());
                        } else {
                            addUsedPublicKey(encodedPublicKey);

                            Cipher cipher = Cipher.getInstance("RSA");
                            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                            Charset encoding = Charset.forName("UTF-8");
                            APIKeyResponse response = new APIKeyResponse(request.getRequestedApiKeyName(), cipher.doFinal(apiKeys.getProperty(request.getRequestedApiKeyName()).getBytes(encoding)), encoding.name());
                            connection.sendTCP(response);
                        }
                    } else {
                        connection.sendTCP(new BadRequestException("Unknown object type"));
                    }
                } catch (Exception e) {
                    FOKLogger.log(Server.class.getName(), Level.SEVERE, "Internal server exception", e);
                    connection.sendTCP(new InternalServerException(ExceptionUtils.getRootCauseMessage(e)));
                }
            }
        });

        kryoServer.start();
    }

    private void addUsedPublicKey(byte[] encodedPubliyKey) {
        usedKeys.add(encodedPubliyKey);
        try {
            saveUsedPublicKeys();
        } catch (FileNotFoundException e) {
            FOKLogger.log(Server.class.getName(), Level.SEVERE, "Could not save the used keys list", e);
        }
    }

    public void readUsedPublicKeys() {
        readUsedPublicKeys(Common.getAndCreateAppDataPath() + fileNameForUsedPublicKeys);
    }

    @SuppressWarnings("unchecked")
    public void readUsedPublicKeys(String absoluteFilePath) {
        try {
            Input input = new Input((new FileInputStream(absoluteFilePath)));
            usedKeys = (ArrayList<byte[]>) getKryoToSaveUsedPublicKeys().readClassAndObject(input);
            input.close();
        } catch (KryoException | ClassCastException | NullPointerException | FileNotFoundException e) {
            FOKLogger.severe(Server.class.getName(), "Unable to read the used public keys list from disk (" + e.getClass().getName() + "), initializing an empty list...");
            File usedKeysFile = new File(absoluteFilePath);
            if (usedKeysFile.exists()) {
                if (!usedKeysFile.delete()) {
                    FOKLogger.severe(Server.class.getName(), "Could not delete the file '" + absoluteFilePath + "', please try to delete it manually or else it might lead to more exceptions");
                }
            }
            usedKeys = new ArrayList<>();
        }
    }

    public void saveUsedPublicKeys() throws FileNotFoundException {
        saveUsedPublicKeys(Common.getAndCreateAppDataPath() + fileNameForUsedPublicKeys);
    }

    public void saveUsedPublicKeys(String absoluteFilePath) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(absoluteFilePath));
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

    public boolean resetPermanently() {
        return resetPermanently(Common.getAndCreateAppDataPath() + fileNameForUsedPublicKeys);
    }

    public boolean resetPermanently(String absolutePathToUsedKeysFile) {
        File usedKeysFile = new File(absolutePathToUsedKeysFile);
        if (usedKeysFile.exists()) {
            if (!usedKeysFile.delete()) {
                FOKLogger.severe(Server.class.getName(), "Failed to delete the file '" + absolutePathToUsedKeysFile + "', server will reset temporarily only.");
                resetTemporarily();
                return false;
            }
        }

        readUsedPublicKeys(absolutePathToUsedKeysFile);
        return true;
    }

    public void resetTemporarily() {
        usedKeys = new ArrayList<>();
    }

    public void stop() {
        kryoServer.stop();
    }
}
