package com.zenandops.dashboard.domain.valueobject;

import java.util.List;

/**
 * Complete dashboard payload aggregating all operational metrics.
 * Returned by the Dashboard_Service as a single JSON response.
 * <p>
 * Individual sections may be {@code null} when the corresponding data provider
 * fails; in that case the failure reason is recorded in the {@code errors} list.
 */
public record DashboardPayload(
        ExecutiveSummary executiveSummary,
        TicketsByState ticketsByState,
        SliSloCompliance sliSloCompliance,
        IncidentMetrics incidentMetrics,
        ErrorBudget errorBudget,
        ChangeManagement changeManagement,
        List<String> errors
) {
}
