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


import org.junit.Test;

import static com.github.vatbub.safeAPIKeyStore.common.InternalExceptionImplTest.testException;

public class BadRequestExceptionTest {
    @Test
    public void emptyExceptionTest() {
        testException(new BadRequestExceptionInternalImpl(), null, null);
    }

    @Test
    public void getMessageTest() {
        String messageString = "test exception";
        testException(new BadRequestExceptionInternalImpl(messageString), messageString, null);
    }

    @Test
    public void getRootCauseMessageTest() {
        String messageString = "test exception";
        String rootCause = "root cause";
        testException(new BadRequestExceptionInternalImpl(messageString, rootCause), messageString, rootCause);
    }
}
