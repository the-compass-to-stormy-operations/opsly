package com.zenandops.dashboard.application.port;

import com.zenandops.dashboard.domain.valueobject.TicketsByState;

/**
 * Outbound port for retrieving ticket counts grouped by ITIL lifecycle state.
 */
public interface TicketMetricsProvider {

    TicketsByState getTicketsByState();
}
