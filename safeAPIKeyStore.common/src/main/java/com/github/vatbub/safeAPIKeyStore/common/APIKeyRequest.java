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
 * Used to request an api key from the server
 */
public class APIKeyRequest {
    private byte[] encodedClientPublicKey;
    private String requestedApiKeyName;

    /**
     * Used by KryoNet
     * @deprecated
     */
    @SuppressWarnings("unused")
    @Deprecated
    public APIKeyRequest() {
        this(null, null);
    }

    /**
     * Creates a new APIKeyRequest
     * @param clientPublicKey The client's public key encoded using {@code keyPair.getPublic().getEncoded()}
     * @param requestedApiKeyName The name of the api key to retrieve
     */
    public APIKeyRequest(byte[] clientPublicKey, String requestedApiKeyName) {
        setEncodedClientPublicKey(clientPublicKey);
        setRequestedApiKeyName(requestedApiKeyName);
    }

    /**
     * The client's public key encoded using {@code keyPair.getPublic().getEncoded()}
     * @return The client's public key encoded using {@code keyPair.getPublic().getEncoded()}
     */
    public byte[] getEncodedClientPublicKey() {
        return encodedClientPublicKey;
    }

    /**
     * Sets the client's public key encoded using {@code keyPair.getPublic().getEncoded()}
     * @param encodedClientPublicKey The client's public key encoded using {@code keyPair.getPublic().getEncoded()}
     */
    public void setEncodedClientPublicKey(byte[] encodedClientPublicKey) {
        this.encodedClientPublicKey = encodedClientPublicKey;
    }

    /**
     * The name of the api key to retrieve
     * @return The name of the api key to retrieve
     */
    public String getRequestedApiKeyName() {
        return requestedApiKeyName;
    }

    /**
     * Sets the name of the api key to retrieve
     * @param requestedApiKeyName The name of the api key to retrieve
     */
    public void setRequestedApiKeyName(String requestedApiKeyName) {
        this.requestedApiKeyName = requestedApiKeyName;
    }
}
