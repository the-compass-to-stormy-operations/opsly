package com.zenandops.auth.infrastructure.rest.dto;

/**
 * Request DTO for the login endpoint.
 *
 * @param login    the user's login identifier
 * @param password the user's password
 */
public record LoginRequest(String login, String password) {
}
