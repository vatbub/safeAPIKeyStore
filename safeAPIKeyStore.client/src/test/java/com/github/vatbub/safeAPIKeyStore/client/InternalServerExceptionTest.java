package com.github.vatbub.safeAPIKeyStore.client;

import org.junit.Assert;
import org.junit.Test;

public class InternalServerExceptionTest {
    @Test
    public void defaultConstructorTest() {
        InternalServerException exception = new InternalServerException();
        Assert.assertNull(exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void messageOnlyTest() {
        String sampleMessage = "fake exception";
        InternalServerException exception = new InternalServerException(sampleMessage);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void causeOnlyTest() {
        IllegalStateException cause = new IllegalStateException("Fake cause");
        InternalServerException exception = new InternalServerException(cause);
        Assert.assertNotNull(exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void messageAndCauseTest() {
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        InternalServerException exception = new InternalServerException(sampleMessage, cause);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void suppressionAndWritableStacktraceTest() {
        // god knows why we need to test this
        String sampleMessage = "fake exception";
        IllegalStateException cause = new IllegalStateException("Fake cause");
        InternalServerException exception = new InternalServerException(sampleMessage, cause, true, true);
        Assert.assertEquals(sampleMessage, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
}
