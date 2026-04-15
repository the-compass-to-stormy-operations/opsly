package com.zenandops.auth.domain.exception;

/**
 * Thrown when a revoked refresh token is used.
 */
public class TokenRevokedException extends RuntimeException {

    public TokenRevokedException() {
        super("Token has been revoked");
    }

    public TokenRevokedException(String message) {
        super(message);
    }
}
