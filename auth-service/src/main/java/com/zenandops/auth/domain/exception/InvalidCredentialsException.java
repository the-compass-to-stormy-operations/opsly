package com.zenandops.auth.domain.exception;

/**
 * Thrown when authentication fails due to invalid login or password.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Authentication failed");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
