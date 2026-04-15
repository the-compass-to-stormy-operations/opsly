package com.zenandops.auth.domain.valueobject;

/**
 * Enumeration of authentication event types published to Kafka.
 */
public enum EventType {
    LOGIN,
    LOGOFF,
    TOKEN_REFRESH
}
