package com.github.vatbub.safeAPIKeyStore;

/*-
 * #%L
 * safeAPIKeyStore.server
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
import com.github.vatbub.safeAPIKeyStore.server.Main;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @BeforeClass
    public static void oneTimeSetUp(){
        Common.getInstance().setAppName("SafeAPIKeyStoreServerTests");
    }

    @Test
    public void mainInstantiationTest() {
        try {
            Constructor<Main> constructor = Main.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("Exception expected");
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void printHelpTest() {
        Main.printHelpMessage(Main.createCommandlineOptions());
        Assert.assertFalse(systemOutRule.getLog().isEmpty());
    }

    @Test
    public void noArgTest() {
        Main.main(new String[0]);
        Assert.assertTrue(systemOutRule.getLog().contains("usage"));
        Main.getServer().stop();
    }

    @Test
    public void portOnlyTest() {
        String[] args = {"-p", "8080"};
        Main.main(args);
        Assert.assertTrue(systemOutRule.getLog().contains("usage"));
    }

    @Test
    public void expandedPortOnlyTest() {
        String[] args = {"--port", "8080"};
        Main.main(args);
        Assert.assertTrue(systemOutRule.getLog().contains("usage"));
    }

    @Test
    public void apiKeyFileOnlyTest() throws IOException {
        File apiKeyFile = SafeAPIKeyStoreTestBase.createApiKeyFile();
        String[] args = {"-apiKeyFile", apiKeyFile.getAbsolutePath()};
        Main.main(args);
        Assert.assertFalse(systemOutRule.getLog().contains("usage"));
    }

    @Test
    public void allArgsTest() throws IOException {
        File apiKeyFile = SafeAPIKeyStoreTestBase.createApiKeyFile();
        String[] args = {"-p", "8080", "-apiKeyFile", apiKeyFile.getAbsolutePath()};
        Main.main(args);
        Assert.assertFalse(systemOutRule.getLog().contains("usage"));
    }

    @Test
    public void illegalApiKeyFileTest(){
        String[] args = {"-p", "8080", "-apiKeyFile", "notExistingDummyFile.properties"};
        Main.main(args);
        Assert.assertFalse(systemErrRule.getLog().contains("Could not launch the server"));
    }
}
