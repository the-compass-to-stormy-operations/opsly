package com.zenandops.dashboard.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ticket counts grouped by ITIL lifecycle state.
 */
public record TicketsByState(
        @JsonProperty("new") int newCount,
        int processingAssigned,
        int processingPlanned,
        int pending,
        int solved,
        int closed
) {
}
