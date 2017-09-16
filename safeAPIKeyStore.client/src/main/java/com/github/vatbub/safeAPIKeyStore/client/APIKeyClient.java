package com.github.vatbub.safeAPIKeyStore.client;

/*-
 * #%L
 * safeAPIKeyStore.client
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


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.vatbub.safeAPIKeyStore.common.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Communicates with an APIKey-server to retrieve api keys. Intended for static use.
 *
 * @author Frederik Kammel
 */
public class APIKeyClient {

    /**
     * Retrieves the specified api key from the specified server. Assumes that the server runs on the default port (1650).
     *
     * @param serverHost The host address of the server.
     * @param apiKeyName The name of the api key to retrieve
     * @return The requested api key or {@code null} in case of unexpected errors.
     * @throws IOException             In case the connection with the server cannot be established or the received key cannot be decrypted.
     * @throws TimeoutException        If the server takes more than 10 seconds time to respond.
     * @throws InternalServerException If something went wrong on the server side
     */
    public static String getApiKey(String serverHost, String apiKeyName) throws IOException, TimeoutException, BadRequestException, InternalServerException, BadPaddingException, IllegalBlockSizeException {
        return getApiKey(serverHost, 1650, apiKeyName);
    }

    /**
     * Retrieves the specified api key from the specified server.
     *
     * @param serverHost The host address of the server.
     * @param serverPort The port that the server is running on
     * @param apiKeyName The name of the api key to retrieve
     * @return The requested api key or {@code null} in case of unexpected errors.
     * @throws IOException             In case the connection with the server cannot be established or the received key cannot be decrypted.
     * @throws TimeoutException        If the server takes more than 10 seconds time to respond.
     * @throws InternalServerException If something went wrong on the server side
     */
    public static String getApiKey(String serverHost, int serverPort, String apiKeyName) throws IOException, TimeoutException, BadRequestException, InternalServerException, BadPaddingException, IllegalBlockSizeException {
        return getApiKey(serverHost, serverPort, apiKeyName, 10000);
    }

    /**
     * Retrieves the specified api key from the specified server.
     *
     * @param serverHost                    The host address of the server.
     * @param serverPort                    The port that the server is running on
     * @param apiKeyName                    The name of the api key to retrieve
     * @param responseTimeoutInMilliSeconds Time in milliseconds after which the request is aborted if no response was received from the server.
     * @return The requested api key or {@code null} in case of unexpected errors.
     * @throws IOException             In case the connection with the server cannot be established or the received key cannot be decrypted.
     * @throws TimeoutException        If the server takes more than the specified time to respond.
     * @throws InternalServerException If something went wrong on the server side
     */
    public static String getApiKey(String serverHost, int serverPort, String apiKeyName, int responseTimeoutInMilliSeconds) throws IOException, TimeoutException, InternalServerException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            APIKeyRequest request = new APIKeyRequest(keyPair.getPublic().getEncoded(), apiKeyName);

            Client kryoClient = new Client();
            KryoCommon.registerClasses(kryoClient.getKryo());
            kryoClient.start();

            final Object[] responseObject = new Object[1];
            kryoClient.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    responseObject[0] = object;
                }
            });

            kryoClient.connect(5000, serverHost, serverPort);
            kryoClient.sendTCP(request);

            // wait for the response
            int sleepTime = responseTimeoutInMilliSeconds / 100;
            int numberOfSleeps = 0;
            while (responseObject[0] == null) {
                if (numberOfSleeps * sleepTime >= responseTimeoutInMilliSeconds) {
                    throw new TimeoutException("Timed out while waiting for a response from the server");
                }
                Thread.sleep(sleepTime);
                numberOfSleeps++;
            }

            // received a response
            Object object = responseObject[0];
            if (object instanceof BadRequestException) {
                throw (BadRequestException) object;
            } else if (object instanceof InternalServerException) {
                throw (InternalServerException) object;
            } else if (object instanceof MultipleRequestsWithSameRSAKeyException) {
                throw (MultipleRequestsWithSameRSAKeyException) object;
            } else if (object instanceof APIKeyResponse) {
                APIKeyResponse response = (APIKeyResponse) object;
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
                return new String(cipher.doFinal(response.getEncryptedAPIKey()), response.getEncoding());
            } else {
                throw new IllegalStateException("Illegal response from server. Server sent object of type " + object.getClass().getName());
            }
        } catch (NoSuchAlgorithmException | InterruptedException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | BadRequestException | IllegalBlockSizeException e) {
            // Should never happen
            e.printStackTrace();
            return null;
        }
    }
}
