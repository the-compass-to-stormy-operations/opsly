package com.zenandops.auth.infrastructure.rest.dto;

/**
 * Response DTO containing the access and refresh tokens.
 *
 * @param accessToken  the JWT access token
 * @param refreshToken the refresh token
 */
public record TokenResponse(String accessToken, String refreshToken) {
}
