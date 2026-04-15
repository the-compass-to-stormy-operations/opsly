package com.zenandops.auth.infrastructure.adapter.persistence;

import com.zenandops.auth.application.port.RefreshTokenRepository;
import com.zenandops.auth.domain.entity.RefreshToken;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * MongoDB Panache adapter implementing the RefreshTokenRepository port.
 */
@ApplicationScoped
public class MongoRefreshTokenRepository implements RefreshTokenRepository {

    @Override
    public void save(RefreshToken token) {
        RefreshTokenPanacheEntity entity = toEntity(token);
        entity.persist();
        token.setId(entity.id.toString());
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return RefreshTokenPanacheEntity.<RefreshTokenPanacheEntity>find("token", token)
                .firstResultOptional()
                .map(this::toDomain);
    }

    @Override
    public void revokeByUserId(String userId) {
        RefreshTokenPanacheEntity.update("revoked", true)
                .where("userId", userId);
    }

    @Override
    public void revokeByToken(String token) {
        RefreshTokenPanacheEntity.update("revoked", true)
                .where("token", token);
    }

    private RefreshToken toDomain(RefreshTokenPanacheEntity entity) {
        RefreshToken token = new RefreshToken();
        token.setId(entity.id.toString());
        token.setToken(entity.token);
        token.setUserId(entity.userId);
        token.setExpiresAt(entity.expiresAt);
        token.setRevoked(entity.revoked);
        token.setCreatedAt(entity.createdAt);
        return token;
    }

    private RefreshTokenPanacheEntity toEntity(RefreshToken token) {
        RefreshTokenPanacheEntity entity = new RefreshTokenPanacheEntity();
        entity.token = token.getToken();
        entity.userId = token.getUserId();
        entity.expiresAt = token.getExpiresAt();
        entity.revoked = token.isRevoked();
        entity.createdAt = token.getCreatedAt();
        return entity;
    }
}
