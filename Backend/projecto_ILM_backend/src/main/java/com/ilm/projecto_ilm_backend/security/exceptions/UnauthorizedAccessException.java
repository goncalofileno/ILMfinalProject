package com.ilm.projecto_ilm_backend.security.exceptions;

/**
 * UnauthorizedAccessException is a custom exception class that extends the RuntimeException class.
 * It is thrown when an unauthorized access is attempted.
 */
public class UnauthorizedAccessException extends RuntimeException {
    /**
     * Constructor for the UnauthorizedAccessException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

