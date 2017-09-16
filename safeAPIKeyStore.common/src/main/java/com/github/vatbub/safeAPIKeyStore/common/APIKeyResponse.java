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


public class APIKeyResponse {
    private String requestedApiKeyName;
    private byte[] encryptedAPIKey;
    private String encoding;

    public APIKeyResponse() {
        this(null, null, null);
    }

    public APIKeyResponse(String requestedApiKeyName, byte[] encryptedAPIKey, String encoding) {
        setEncoding(encoding);
        setRequestedApiKeyName(requestedApiKeyName);
        setEncryptedAPIKey(encryptedAPIKey);
    }

    public String getRequestedApiKeyName() {
        return requestedApiKeyName;
    }

    public void setRequestedApiKeyName(String requestedApiKeyName) {
        this.requestedApiKeyName = requestedApiKeyName;
    }

    public byte[] getEncryptedAPIKey() {
        return encryptedAPIKey;
    }

    public void setEncryptedAPIKey(byte[] encryptedAPIKey) {
        this.encryptedAPIKey = encryptedAPIKey;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
