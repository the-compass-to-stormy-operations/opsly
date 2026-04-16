package com.zenandops.auth.infrastructure.adapter.persistence;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

/**
 * MongoDB Panache entity mapping for the RefreshToken domain entity.
 */
@MongoEntity(collection = "refresh_tokens")
public class RefreshTokenPanacheEntity extends PanacheMongoEntity {

    public String token;

    @BsonProperty("userId")
    public String userId;

    @BsonProperty("expiresAt")
    public Instant expiresAt;

    public boolean revoked = false;

    @BsonProperty("createdAt")
    public Instant createdAt;
}
