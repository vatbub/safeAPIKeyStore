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


/**
 * Returned by the server if something went wrong on the server side
 */
public class InternalServerExceptionInternalImpl extends ExceptionInternalImpl {
    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public InternalServerExceptionInternalImpl() {
        super();
    }

    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public InternalServerExceptionInternalImpl(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public InternalServerExceptionInternalImpl(String message, String rootCauseMessage) {
        super(message, rootCauseMessage);
    }
}