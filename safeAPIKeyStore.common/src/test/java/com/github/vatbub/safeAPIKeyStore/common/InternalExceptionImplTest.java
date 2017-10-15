package com.github.vatbub.safeAPIKeyStore.common;

import org.junit.Assert;
import org.junit.Test;

public class InternalExceptionImplTest {
    @Test
    public void emptyExceptionTest() {
        testException(new ExceptionInternalImpl(), null, null);
    }

    @Test
    public void getMessageTest() {
        String messageString = "test exception";
        testException(new ExceptionInternalImpl(messageString), messageString, null);
    }

    @Test
    public void getRootCauseMessageTest() {
        String messageString = "test exception";
        String rootCause = "root cause";
        testException(new ExceptionInternalImpl(messageString, rootCause), messageString, rootCause);
    }

    @Test
    public void emptyBadRequestExceptionTest(){

    }

    public static void testException(ExceptionInternalImpl exception, String expectedMessage, String expectedRootMessage) {
        Assert.assertNotNull(exception);
        Assert.assertEquals(expectedMessage, exception.getMessage());
        Assert.assertEquals(expectedRootMessage, exception.getRootCauseMessage());
    }
}
