package com.zenandops.auth.domain.valueobject;

import java.time.Instant;

/**
 * Value object representing an authentication event to be published to Kafka.
 *
 * @param eventId   unique identifier for the event (UUID)
 * @param eventType the type of authentication event
 * @param userId    the user's identifier
 * @param userLogin the user's login name
 * @param timestamp when the event occurred
 */
public record AuthEvent(
        String eventId,
        EventType eventType,
        String userId,
        String userLogin,
        Instant timestamp
) {
}
