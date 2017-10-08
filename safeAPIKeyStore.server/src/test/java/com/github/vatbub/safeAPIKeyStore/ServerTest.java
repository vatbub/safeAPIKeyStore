package com.github.vatbub.safeAPIKeyStore;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.vatbub.safeAPIKeyStore.common.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ServerTest extends SafeAPIKeyStoreTestBase {
    private static Client client;
    private List<Listener> listeners = new LinkedList<>();
    private List<Throwable> exceptionsThrownFromOtherThreads = new LinkedList<>();
    private Thread shutDownThread;
    private boolean shutClientDown = false;

    @After
    public void tearDownClient() {
        client.stop();
    }

    @Before
    public void setUpClient() throws IOException, InterruptedException {
        // set up the client
        client = new Client();
        KryoCommon.registerClasses(client.getKryo());
        client.getKryo().setReferences(true);
        client.start();

        client.connect(100000, "localhost", port);

        shutDownThread = new Thread(() -> {
            boolean isShutDown = false;
            while (!isShutDown) {
                System.out.print("");
                if (shutClientDown) {
                    try {
                        // shut client down
                        client.close();
                        Thread.sleep(5000);
                        isShutDown = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        shutDownThread.setName("shutDownThread");
        shutDownThread.start();
    }

    @Before
    public void resetClientListeners() {
        while (listeners.size() > 0) {
            client.removeListener(listeners.remove(0));
        }
    }

    @After
    public void failIfExceptionThrown() {
        boolean fail = exceptionsThrownFromOtherThreads.size() > 0;
        while (exceptionsThrownFromOtherThreads.size() > 0) {
            exceptionsThrownFromOtherThreads.remove(0).printStackTrace();
        }
        if (fail) {
            Assert.fail("Exceptions were reported from other threads");
        }
    }

    public void createListener(Listener listener) {
        listeners.add(listener);
        client.addListener(listener);
    }

    @Test
    public void regularRequestTest() throws NoSuchAlgorithmException, InterruptedException {
        KeyPair keyPair = generateKeyPair();

        createListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                try {
                    Charset encoding = Charset.forName("UTF-8");

                    Assert.assertTrue(object instanceof APIKeyResponse);
                    APIKeyResponse response = (APIKeyResponse) object;
                    Assert.assertEquals(apiKey1Name, response.getRequestedApiKeyName());
                    Assert.assertEquals(encoding.name(), response.getEncoding());

                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

                    Assert.assertEquals(apiKey1Value, new String(cipher.doFinal(response.getEncryptedAPIKey()), response.getEncoding()));
                    endTest();
                } catch (Throwable e) {
                    exceptionsThrownFromOtherThreads.add(e);
                    endTest();
                }

            }
        });

        APIKeyRequest request = new APIKeyRequest(keyPair.getPublic().getEncoded(), apiKey1Name);
        client.sendTCP(request);
        shutDownThread.join();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Test
    public void multipleRequestsWithSamePublicKeyTest() throws NoSuchAlgorithmException, InterruptedException {
        KeyPair keyPair = generateKeyPair();
        APIKeyRequest request = new APIKeyRequest(keyPair.getPublic().getEncoded(), apiKey1Name);

        createListener(new Listener() {
            private int callCounter = 0;

            @Override
            public void received(Connection connection, Object object) {
                try {
                    if (callCounter == 0) {
                        Assert.assertTrue(object instanceof APIKeyResponse);
                        client.sendTCP(request);
                    } else {
                        Assert.assertTrue(object instanceof MultipleRequestsWithSameRSAKeyExceptionInternalImpl);
                        endTest();
                    }
                    callCounter++;
                } catch (Throwable e) {
                    exceptionsThrownFromOtherThreads.add(e);
                    endTest();
                }

            }
        });

        client.sendTCP(request);
        shutDownThread.join();
    }

    @Test
    public void badRequestTest() throws NoSuchAlgorithmException, InterruptedException {
        createListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                try {
                    Assert.assertTrue(object instanceof BadRequestExceptionInternalImpl);
                    endTest();
                } catch (Throwable e) {
                    exceptionsThrownFromOtherThreads.add(e);
                    endTest();
                }

            }
        });

        Charset encoding = Charset.forName("UTF-8");
        client.sendTCP(new APIKeyResponse("blablub", "bbb".getBytes(encoding), encoding.name()));
        shutDownThread.join();
    }

    @Test
    public void internalServerErrorTest() throws NoSuchAlgorithmException, InterruptedException {
        createListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                try {
                    Assert.assertTrue(object instanceof InternalServerExceptionInternalImpl);
                    endTest();
                } catch (Throwable e) {
                    exceptionsThrownFromOtherThreads.add(e);
                    endTest();
                }

            }
        });

        byte[] publicKey = {0, 1};
        client.sendTCP(new APIKeyRequest(publicKey, apiKey1Name));
        shutDownThread.join();
    }

    /**
     * Ends the currently running test
     */
    private void endTest() {
        shutClientDown = true;
    }
}
