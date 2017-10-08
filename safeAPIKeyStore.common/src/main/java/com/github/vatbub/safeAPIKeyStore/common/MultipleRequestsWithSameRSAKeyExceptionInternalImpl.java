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


public class MultipleRequestsWithSameRSAKeyExceptionInternalImpl extends ExceptionInternalImpl {
    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl() {
        super();
    }

    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl(String message, String rootCauseMessage) {
        super(message, rootCauseMessage);
    }
}
