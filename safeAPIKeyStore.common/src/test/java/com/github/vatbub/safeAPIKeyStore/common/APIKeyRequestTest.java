package com.github.vatbub.safeAPIKeyStore.common;

import org.junit.Assert;
import org.junit.Test;

public class APIKeyRequestTest {
    @SuppressWarnings("deprecation")
    @Test
    public void defaultConstructorTest() {
        APIKeyRequest request = new APIKeyRequest();
        Assert.assertNull(request.getRequestedApiKeyName());
        Assert.assertNull(request.getEncodedClientPublicKey());
    }

    @Test
    public void valueTest() {
        byte[] key = {0, 1};
        String apiKeyName = "test";
        APIKeyRequest request = new APIKeyRequest(key, apiKeyName);
        Assert.assertEquals(apiKeyName, request.getRequestedApiKeyName());
        Assert.assertEquals(key, request.getEncodedClientPublicKey());
    }
}
