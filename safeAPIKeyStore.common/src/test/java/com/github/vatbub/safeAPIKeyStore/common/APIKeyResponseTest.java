package com.github.vatbub.safeAPIKeyStore.common;

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
