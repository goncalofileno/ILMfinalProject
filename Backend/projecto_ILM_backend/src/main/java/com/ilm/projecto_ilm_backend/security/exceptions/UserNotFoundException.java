package com.ilm.projecto_ilm_backend.security.exceptions;

/**
 * UserNotFoundException is a custom exception class that extends the Exception class.
 * It is thrown when a user is not found.
 */
public class UserNotFoundException extends Exception {
    /**
     * Constructor for the UserNotFoundException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
