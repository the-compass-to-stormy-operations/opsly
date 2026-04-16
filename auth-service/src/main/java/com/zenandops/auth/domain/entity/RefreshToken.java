package com.zenandops.auth.domain.entity;

import java.time.Instant;

/**
 * RefreshToken entity representing a long-lived token used for session renewal.
 * Designed as a mutable class for MongoDB Panache compatibility.
 */
public class RefreshToken {

    private String id;
    private String token;
    private String userId;
    private Instant expiresAt;
    private boolean revoked;
    private Instant createdAt;

    public RefreshToken() {
        this.revoked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if this refresh token has expired.
     */
    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
}
