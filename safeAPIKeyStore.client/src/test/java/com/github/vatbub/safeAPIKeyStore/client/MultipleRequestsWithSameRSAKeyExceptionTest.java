package com.github.vatbub.safeAPIKeyStore.client;

import org.junit.Assert;
import org.junit.Test;

public class MultipleRequestsWithSameRSAKeyExceptionTest {
    @Test
    public void defaultConstructorTest() {
        MultipleRequestsWithSameRSAKeyException exception = new MultipleRequestsWithSameRSAKeyException();
        Assert.assertNull(exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void messageOnlyTest() {
        String sampleMessage = "fake exception";
        MultipleRequestsWithSameRSAKeyException exception = new MultipleRequestsWithSameRSAKeyException(sampleMessage);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void causeOnlyTest() {
        IllegalStateException cause = new IllegalStateException("Fake cause");
        MultipleRequestsWithSameRSAKeyException exception = new MultipleRequestsWithSameRSAKeyException(cause);
        Assert.assertNotNull(exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void messageAndCauseTest() {
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        MultipleRequestsWithSameRSAKeyException exception = new MultipleRequestsWithSameRSAKeyException(sampleMessage, cause);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void suppressionAndWritableStacktraceTest() {
        // god knows why we need to test this
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        MultipleRequestsWithSameRSAKeyException exception = new MultipleRequestsWithSameRSAKeyException(sampleMessage, cause, true, true);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
}
