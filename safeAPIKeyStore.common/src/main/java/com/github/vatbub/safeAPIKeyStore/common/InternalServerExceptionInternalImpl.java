package com.github.vatbub.safeAPIKeyStore.common;

/**
 * Returned by the server if something went wrong on the server side
 */
public class InternalServerExceptionInternalImpl extends ExceptionInternalImpl {
    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public InternalServerExceptionInternalImpl() {
        super();
    }

    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public InternalServerExceptionInternalImpl(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InternalServerExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public InternalServerExceptionInternalImpl(String message, String rootCauseMessage) {
        super(message, rootCauseMessage);
    }
}