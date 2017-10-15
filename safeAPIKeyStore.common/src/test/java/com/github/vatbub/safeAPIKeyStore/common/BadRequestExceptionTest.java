package com.github.vatbub.safeAPIKeyStore.common;

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
