package com.ilm.projecto_ilm_backend.security.exceptions;
/**
 * NoProjectsForInviteeException is a custom exception class that extends the Exception class.
 * It is thrown when there are no projects for the invitee.
 */
public class NoProjectsForInviteeException extends Exception {
    /**
     * Constructor for the NoProjectsForInviteeException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NoProjectsForInviteeException(String message) {
        super(message);
    }
}

