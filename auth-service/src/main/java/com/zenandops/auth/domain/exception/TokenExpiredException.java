package com.zenandops.auth.domain.exception;

/**
 * Thrown when a token (access or refresh) has expired.
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("Token has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
