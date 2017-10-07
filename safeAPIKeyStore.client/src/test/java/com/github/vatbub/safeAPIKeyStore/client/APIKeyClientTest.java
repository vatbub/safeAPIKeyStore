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


import com.github.vatbub.safeAPIKeyStore.SafeAPIKeyStoreTestBase;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class APIKeyClientTest extends SafeAPIKeyStoreTestBase {
    @Test
    public void getAPIKeysTest() throws BadPaddingException, BadRequestException, IOException, TimeoutException, IllegalBlockSizeException, InternalServerException {
        String apiKey1 = APIKeyClient.getApiKey("localhost", port, apiKey1Name);
        Assert.assertEquals(apiKey1Value, apiKey1);

        String apiKey2 = APIKeyClient.getApiKey("localhost", port, apiKey2Name);
        Assert.assertEquals(apiKey2Value, apiKey2);
    }
}
