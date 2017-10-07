package com.github.vatbub.safeAPIKeyStore.common;

public class BadRequestExceptionInternalImpl extends ExceptionInternalImpl {
    /**
     * Constructs a new {@code BadRequestExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public BadRequestExceptionInternalImpl() {
        super();
    }

    /**
     * Constructs a new {@code BadRequestExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public BadRequestExceptionInternalImpl(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code BadRequestExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public BadRequestExceptionInternalImpl(String message, String rootCauseMessage) {
        super(message, rootCauseMessage);
    }
}
