package com.github.vatbub.safeAPIKeyStore.client;

import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class APIClientExceptionTests {
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
        Thread.sleep(5000);
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
}
