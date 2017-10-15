package com.github.vatbub.safeAPIKeyStore.client;

/*-
 * #%L
 * safeAPIKeyStore.client
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

public class BadRequestExceptionTest {
    @Test
    public void defaultConstructorTest() {
        BadRequestException exception = new BadRequestException();
        Assert.assertNull(exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void messageOnlyTest() {
        String sampleMessage = "fake exception";
        BadRequestException exception = new BadRequestException(sampleMessage);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void causeOnlyTest() {
        IllegalStateException cause = new IllegalStateException("Fake cause");
        BadRequestException exception = new BadRequestException(cause);
        Assert.assertNotNull(exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void messageAndCauseTest() {
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        BadRequestException exception = new BadRequestException(sampleMessage, cause);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void suppressionAndWritableStacktraceTest() {
        // god knows why we need to test this
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        BadRequestException exception = new BadRequestException(sampleMessage, cause, true, true);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
}
