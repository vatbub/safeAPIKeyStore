package com.github.vatbub.safeAPIKeyStore.common;

public class MultipleRequestsWithSameRSAKeyExceptionInternalImpl extends ExceptionInternalImpl {
    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl() {
        super();
    }

    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MultipleRequestsWithSameRSAKeyExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public MultipleRequestsWithSameRSAKeyExceptionInternalImpl(String message, String rootCauseMessage) {
        super(message, rootCauseMessage);
    }
}
