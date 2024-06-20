package com.ilm.projecto_ilm_backend.security.exceptions;

public class UserNotInProjectException extends Exception {
    public UserNotInProjectException(String message) {
        super(message);
    }
}
