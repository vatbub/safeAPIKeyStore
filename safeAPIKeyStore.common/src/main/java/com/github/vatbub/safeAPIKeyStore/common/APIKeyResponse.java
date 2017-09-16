package com.github.vatbub.safeAPIKeyStore.common;

/*-
 * #%L
 * com.github.vatbub.common
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

/**
 * The server's response to a {@link APIKeyRequest}
 */
public class APIKeyResponse {
    private String requestedApiKeyName;
    private byte[] encryptedAPIKey;
    private String encoding;

    /**
     * Used by KryoNet
     * @deprecated
     */
    @SuppressWarnings("unused")
    public APIKeyResponse() {
        this(null, null, null);
    }

    /**
     * Creates a new APIKeyResponse
     * @param requestedApiKeyName The name of the requested api key
     * @param encryptedAPIKey The RSA-encrypted api key
     * @param encoding The encoding to be used to decode the decrypted api key.
     */
    public APIKeyResponse(String requestedApiKeyName, byte[] encryptedAPIKey, String encoding) {
        setEncoding(encoding);
        setRequestedApiKeyName(requestedApiKeyName);
        setEncryptedAPIKey(encryptedAPIKey);
    }

    /**
     * The name of the requested api key
     * @return The name of the requested api key
     */
    public String getRequestedApiKeyName() {
        return requestedApiKeyName;
    }

    /**
     * Sets the name of the requested api key
     * @param requestedApiKeyName The name of the requested api key
     */
    public void setRequestedApiKeyName(String requestedApiKeyName) {
        this.requestedApiKeyName = requestedApiKeyName;
    }

    /**
     * The RSA-encrypted api key
     * @return The RSA-encrypted api key
     */
    public byte[] getEncryptedAPIKey() {
        return encryptedAPIKey;
    }

    /**
     * Sets the RSA-encrypted api key
     * @param encryptedAPIKey The RSA-encrypted api key
     */
    public void setEncryptedAPIKey(byte[] encryptedAPIKey) {
        this.encryptedAPIKey = encryptedAPIKey;
    }

    /**
     * The encoding to be used to decode the decrypted api key.
     * @return The encoding to be used to decode the decrypted api key.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding to be used to decode the decrypted api key.
     * @param encoding The encoding to be used to decode the decrypted api key.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
