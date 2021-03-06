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


import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.awaitility.Awaitility.await;

public class APIClientExceptionTest {
    public static final String apiKey1Name = "apiKey1";
    public static final String apiKey1Value = "12345";
    public static final String apiKey2Name = "apiKey2";
    public static final String apiKey2Value = "vfcdr567zutrf76t8uiohjläkB;NLM:,-jlhkgi&(RTZF)/UOHJLÄBKVGJCuftr86ZUOGHÄhjklbnvgjcUTFDr6zi";
    public static final int port = 1650;
    private static final String apiKeysFileName = "apiKeysForTesting.properties";
    private static FakeServer server;

    public static FakeServer getServer() {
        return server;
    }

    @BeforeClass
    public static void oneTimeSetUp() throws InterruptedException, IOException, IllegalAccessException {
        Common.getInstance().setAppName("SafeAPIKeyStoreServerTests");

        // Create the api key file
        File apiKeyFile = new File(Common.getInstance().getAndCreateAppDataPath() + apiKeysFileName);
        if (apiKeyFile.exists()) {
            Files.delete(apiKeyFile.toPath());
        }
        Properties apiKeysForTesting = new Properties();
        apiKeysForTesting.setProperty(apiKey1Name, apiKey1Value);
        apiKeysForTesting.setProperty(apiKey2Name, apiKey2Value);
        apiKeysForTesting.store(new FileWriter(apiKeyFile), "API Keys for unit tests of " + Common.getInstance().getAppName());

        FOKLogger.info(com.github.vatbub.safeAPIKeyStore.SafeAPIKeyStoreTestBase.class.getName(), "Launching server...");
        server = new FakeServer(port, apiKeyFile.getAbsolutePath(), FakeServer.FakeResponse.TRUE_ANSWER);
        await().atMost(5, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void oneTimeTeardown() {
        FOKLogger.info(com.github.vatbub.safeAPIKeyStore.SafeAPIKeyStoreTestBase.class.getName(), "Shutting server down...");
        server.stop();
    }

    @Before
    public void setUp() {
        server.resetPermanently();
    }

    @Test
    public void timeoutTest() {
        try {
            getServer().setFakeResponse(FakeServer.FakeResponse.TIMEOUT);
            APIKeyClient.getApiKey("localhost", port, apiKey1Name);
            Assert.fail("Timeout expected");
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InternalServerException | IOException e) {
            e.printStackTrace();
            Assert.fail("Unexpected exception thrown");
        }
    }

    @Test
    public void badRequestTest() throws InternalServerException, TimeoutException, IOException {
        getServer().setFakeResponse(FakeServer.FakeResponse.BAD_REQUEST_EXCEPTION);
        Assert.assertNull(APIKeyClient.getApiKey("localhost", port, apiKey1Name));
    }

    @Test
    public void internalServerExceptionTest() {
        try {
            getServer().setFakeResponse(FakeServer.FakeResponse.INTERNAL_SERVER_EXCEPTION);
            APIKeyClient.getApiKey("localhost", port, apiKey1Name);
            Assert.fail("InternalServerException expected");
        } catch (InternalServerException e) {
            e.printStackTrace();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
            Assert.fail("Unexpected exception thrown");
        }
    }

    @Test
    public void multipleRequestsWithSameRSAKeyExceptionTest() {
        try {
            getServer().setFakeResponse(FakeServer.FakeResponse.MULTIPLE_REQUESTS_WITH_SAME_RSA_KEY_EXCEPTION);
            APIKeyClient.getApiKey("localhost", port, apiKey1Name);
            Assert.fail("MultipleRequestsWithSameRSAKeyException expected");
        } catch (MultipleRequestsWithSameRSAKeyException e) {
            e.printStackTrace();
        } catch (InternalServerException | TimeoutException | IOException e) {
            e.printStackTrace();
            Assert.fail("Unexpected exception thrown");
        }
    }

    @Test
    public void illegalResponseTest() {
        try {
            getServer().setFakeResponse(FakeServer.FakeResponse.ILLEGAL_OBJECT);
            APIKeyClient.getApiKey("localhost", port, apiKey1Name);
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (InternalServerException | TimeoutException | IOException e) {
            e.printStackTrace();
            Assert.fail("Unexpected exception thrown");
        }
    }
}
