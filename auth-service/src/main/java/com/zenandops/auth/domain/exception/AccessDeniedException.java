package com.zenandops.auth.domain.exception;

/**
 * Thrown when a user lacks the required role or attributes to access a resource.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Access denied");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
