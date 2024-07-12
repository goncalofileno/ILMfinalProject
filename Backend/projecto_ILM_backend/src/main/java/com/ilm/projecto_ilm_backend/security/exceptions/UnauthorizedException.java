package com.ilm.projecto_ilm_backend.security.exceptions;

/**
 * UnauthorizedException is a custom exception class that extends the RuntimeException class.
 * It is thrown when an unauthorized action is attempted.
 */
public class UnauthorizedException extends RuntimeException {
    /**
     * Constructor for the UnauthorizedException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}

