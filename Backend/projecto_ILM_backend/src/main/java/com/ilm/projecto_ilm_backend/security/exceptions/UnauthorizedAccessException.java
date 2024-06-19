package com.ilm.projecto_ilm_backend.security.exceptions;


public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

