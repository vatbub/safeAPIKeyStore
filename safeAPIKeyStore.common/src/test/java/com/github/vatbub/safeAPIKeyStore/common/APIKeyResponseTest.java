package com.github.vatbub.safeAPIKeyStore.common;

/*-
 * #%L
 * safeAPIKeyStore.common
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


import org.junit.Assert;
import org.junit.Test;

public class APIKeyResponseTest {
    @SuppressWarnings("deprecation")
    @Test
    public void defaultConstructorTest() {
        APIKeyResponse response = new APIKeyResponse();
        Assert.assertNull(response.getEncoding());
        Assert.assertNull(response.getEncryptedAPIKey());
        Assert.assertNull(response.getRequestedApiKeyName());
    }

    @Test
    public void valueTest() {
        byte[] key = {0, 1};
        String apiKeyName = "test";
        String encoding = "UTF-8";
        APIKeyResponse response = new APIKeyResponse(apiKeyName, key, encoding);
        Assert.assertEquals(apiKeyName, response.getRequestedApiKeyName());
        Assert.assertEquals(key, response.getEncryptedAPIKey());
        Assert.assertEquals(encoding, response.getEncoding());
    }
}
