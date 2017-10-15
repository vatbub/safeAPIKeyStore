package com.github.vatbub.safeAPIKeyStore.common;

import org.junit.Test;

import static com.github.vatbub.safeAPIKeyStore.common.InternalExceptionImplTest.testException;

public class MultipleRequestsWithSameRSAKeyExceptionTest {
    @Test
    public void emptyExceptionTest() {
        testException(new MultipleRequestsWithSameRSAKeyExceptionInternalImpl(), null, null);
    }

    @Test
    public void getMessageTest() {
        String messageString = "test exception";
        testException(new MultipleRequestsWithSameRSAKeyExceptionInternalImpl(messageString), messageString, null);
    }

    @Test
    public void getRootCauseMessageTest() {
        String messageString = "test exception";
        String rootCause = "root cause";
        testException(new MultipleRequestsWithSameRSAKeyExceptionInternalImpl(messageString, rootCause), messageString, rootCause);
    }
}
