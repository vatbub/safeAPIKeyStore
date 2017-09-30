package com.github.vatbub.safeAPIKeyStore;

/*-
 * #%L
 * safeAPIKeyStore.unitTests
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
import com.github.vatbub.safeAPIKeyStore.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public abstract class SafeAPIKeyStoreTestBase {
    public static final String apiKey1Name = "apiKey1";
    public static final String apiKey1Value = "12345";
    public static final String apiKey2Name = "apiKey2";
    public static final String apiKey2Value = "vfcdr567zutrf76t8uiohjläkB;NLM:,-jlhkgi&(RTZF)/UOHJLÄBKVGJCuftr86ZUOGHÄhjklbnvgjcUTFDr6zi";
    public static final int port = 1650;
    private static final String apiKeysFileName = "apiKeysForTesting.properties";
    private static Server server;

    @BeforeClass
    public static void oneTimeSetUp() throws InterruptedException, IOException, IllegalAccessException {
        Common.getInstance().setAppName("SafeAPIKeyStoreServerTests");

        // Create the api key file
        File apiKeyFile = new File(Common.getInstance().getAndCreateAppDataPath() + apiKeysFileName);
        if (apiKeyFile.exists()) {
            if (!apiKeyFile.delete()) {
                throw new IllegalAccessException("Unable to delete the file '" + apiKeyFile.getAbsolutePath() + "'");
            }
        }
        Properties apiKeysForTesting = new Properties();
        apiKeysForTesting.setProperty(apiKey1Name, apiKey1Value);
        apiKeysForTesting.setProperty(apiKey2Name, apiKey2Value);
        apiKeysForTesting.store(new FileWriter(apiKeyFile), "API Keys for unit tests of " + Common.getInstance().getAppName());

        FOKLogger.info(SafeAPIKeyStoreTestBase.class.getName(), "Launching server...");
        server = new Server(port, apiKeyFile.getAbsolutePath());
        Thread.sleep(5000);
    }

    @AfterClass
    public static void oneTimeTeardown() {
        FOKLogger.info(SafeAPIKeyStoreTestBase.class.getName(), "Shutting server down...");
        server.stop();
    }

    @Before
    public void setUp() {
        server.resetPermanently();
    }
}
