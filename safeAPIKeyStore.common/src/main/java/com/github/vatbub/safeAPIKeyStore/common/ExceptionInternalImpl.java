package com.github.vatbub.safeAPIKeyStore.common;

public class ExceptionInternalImpl {
    private String message;
    private String rootCauseMessage;

    /**
     * Constructs a new {@code ExceptionInternalImpl} with {@code null} as its
     * detail message.
     */
    public ExceptionInternalImpl() {
        this(null);
    }

    /**
     * Constructs a new {@code ExceptionInternalImpl} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ExceptionInternalImpl(String message) {
        this(message, null);
    }

    /**
     * Constructs a new {@code ExceptionInternalImpl} with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message          the detail message
     * @param rootCauseMessage the message of the root cause
     */
    public ExceptionInternalImpl(String message, String rootCauseMessage) {
        setMessage(message);
        setRootCauseMessage(rootCauseMessage);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRootCauseMessage() {
        return rootCauseMessage;
    }

    public void setRootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage;
    }
}
