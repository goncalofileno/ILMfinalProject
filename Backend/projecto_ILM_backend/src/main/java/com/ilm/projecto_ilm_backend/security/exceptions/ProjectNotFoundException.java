package com.ilm.projecto_ilm_backend.security.exceptions;
/**
 * ProjectNotFoundException is a custom exception class that extends the Exception class.
 * It is thrown when a project is not found.
 */
public class ProjectNotFoundException extends Exception {
    /**
     * Constructor for the ProjectNotFoundException class.
     * It calls the superclass constructor with the provided message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
