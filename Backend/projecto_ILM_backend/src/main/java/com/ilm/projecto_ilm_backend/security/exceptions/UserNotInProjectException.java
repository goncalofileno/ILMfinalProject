package com.ilm.projecto_ilm_backend.security.exceptions;

/**
 * UserNotInProjectException is a custom exception class that extends the Exception class.
 * It is thrown when a user is not part of a project.
 */
public class UserNotInProjectException extends Exception {
    /**
     * Constructor for the UserNotInProjectException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public UserNotInProjectException(String message) {
        super(message);
    }
}
