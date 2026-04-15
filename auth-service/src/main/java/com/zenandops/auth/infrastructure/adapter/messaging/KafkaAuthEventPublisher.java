package com.zenandops.auth.infrastructure.adapter.messaging;

import com.zenandops.auth.application.port.AuthEventPublisher;
import com.zenandops.auth.domain.valueobject.AuthEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

/**
 * Kafka adapter implementing the AuthEventPublisher port using SmallRye Reactive Messaging.
 * Gracefully handles Kafka unavailability by logging and continuing.
 */
@ApplicationScoped
public class KafkaAuthEventPublisher implements AuthEventPublisher {

    private static final Logger LOG = Logger.getLogger(KafkaAuthEventPublisher.class);

    @Inject
    @Channel("auth-events")
    Emitter<String> emitter;

    private final ObjectMapper objectMapper;

    public KafkaAuthEventPublisher() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void publish(AuthEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            emitter.send(json);
            LOG.infof("Published auth event: type=%s, userId=%s", event.eventType(), event.userId());
        } catch (Exception e) {
            LOG.warnf("Failed to publish auth event: type=%s, userId=%s, reason=%s",
                    event.eventType(), event.userId(), e.getMessage());
        }
    }
}
