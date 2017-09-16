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

public class APIKeyClient {
    public static String getApiKey(String serverHost, int serverPort, String apiKeyName) throws IOException, TimeoutException, BadRequestException, InternalServerException, BadPaddingException, IllegalBlockSizeException {
        return getApiKey(serverHost, serverPort, apiKeyName, 10000);
    }

    public static String getApiKey(String serverHost, int serverPort, String apiKeyName, int responseTimeoutInMilliSeconds) throws IOException, TimeoutException, BadRequestException, InternalServerException, BadPaddingException, IllegalBlockSizeException {
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
        } catch (NoSuchAlgorithmException | InterruptedException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
