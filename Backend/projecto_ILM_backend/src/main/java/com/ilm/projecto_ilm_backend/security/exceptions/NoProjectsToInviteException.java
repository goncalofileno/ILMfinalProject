package com.ilm.projecto_ilm_backend.security.exceptions;
/**
 * NoProjectsToInviteException is a custom exception class that extends the RuntimeException class.
 * It is thrown when there are no projects to invite a user to.
 */
public class NoProjectsToInviteException extends RuntimeException {
    /**
     * Constructor for the NoProjectsToInviteException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NoProjectsToInviteException(String message) {
        super(message);
    }
}
