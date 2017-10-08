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


public class ExceptionInternalImpl {
    private String message;
    private String rootCauseMessage;

    /**
     * Constructs a new {@code ExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public ExceptionInternalImpl() {
        this(null);
    }

    /**
     * Constructs a new {@code ExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ExceptionInternalImpl(String message) {
        this(message, null);
    }

    /**
     * Constructs a new {@code ExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public ExceptionInternalImpl(String message, String rootCauseMessage) {
        setMessage(message);
        setRootCauseMessage(rootCauseMessage);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRootCauseMessage() {
        return rootCauseMessage;
    }

    public void setRootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage;
    }
}
