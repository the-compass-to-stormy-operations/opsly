package com.zenandops.auth.infrastructure.rest.dto;

/**
 * Request DTO for the refresh and logoff endpoints.
 *
 * @param refreshToken the refresh token string
 */
public record RefreshRequest(String refreshToken) {
}
