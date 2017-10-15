package com.github.vatbub.safeAPIKeyStore.common;

import org.junit.Test;

import static com.github.vatbub.safeAPIKeyStore.common.InternalExceptionImplTest.testException;

public class InternalServerExceptionTest {
    @Test
    public void emptyExceptionTest() {
        testException(new InternalServerExceptionInternalImpl(), null, null);
    }

    @Test
    public void getMessageTest() {
        String messageString = "test exception";
        testException(new InternalServerExceptionInternalImpl(messageString), messageString, null);
    }

    @Test
    public void getRootCauseMessageTest() {
        String messageString = "test exception";
        String rootCause = "root cause";
        testException(new InternalServerExceptionInternalImpl(messageString, rootCause), messageString, rootCause);
    }
}
