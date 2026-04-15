package com.zenandops.dashboard.domain.valueobject;

/**
 * Executive summary of operational metrics for the dashboard.
 */
public record ExecutiveSummary(
        int totalOpenTickets,
        int criticalIncidents,
        double overallAvailability,
        double errorBudgetRemaining
) {
}
