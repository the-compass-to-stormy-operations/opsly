package com.zenandops.dashboard.domain.valueobject;

/**
 * Ticket counts grouped by ITIL lifecycle state.
 */
public record TicketsByState(
        int newCount,
        int processingAssigned,
        int processingPlanned,
        int pending,
        int solved,
        int closed
) {
}
