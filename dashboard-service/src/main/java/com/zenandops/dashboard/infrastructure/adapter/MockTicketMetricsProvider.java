package com.zenandops.dashboard.infrastructure.adapter;

import com.zenandops.dashboard.application.port.TicketMetricsProvider;
import com.zenandops.dashboard.domain.valueobject.TicketsByState;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mock implementation of {@link TicketMetricsProvider} returning realistic
 * ITIL ticket counts by lifecycle state.
 */
@ApplicationScoped
public class MockTicketMetricsProvider implements TicketMetricsProvider {

    @Override
    public TicketsByState getTicketsByState() {
        return new TicketsByState(28, 35, 22, 18, 31, 8);
    }
}
