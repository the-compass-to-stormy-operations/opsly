package com.zenandops.dashboard.application.usecase;

import com.zenandops.dashboard.application.port.ChangeMetricsProvider;
import com.zenandops.dashboard.application.port.IncidentMetricsProvider;
import com.zenandops.dashboard.application.port.SliSloMetricsProvider;
import com.zenandops.dashboard.application.port.TicketMetricsProvider;
import com.zenandops.dashboard.domain.valueobject.ChangeManagement;
import com.zenandops.dashboard.domain.valueobject.DashboardPayload;
import com.zenandops.dashboard.domain.valueobject.ErrorBudget;
import com.zenandops.dashboard.domain.valueobject.ExecutiveSummary;
import com.zenandops.dashboard.domain.valueobject.IncidentMetrics;
import com.zenandops.dashboard.domain.valueobject.SliSloCompliance;
import com.zenandops.dashboard.domain.valueobject.TicketsByState;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use case that aggregates data from all metric providers into a single {@link DashboardPayload}.
 * <p>
 * Each provider is called independently. When a provider fails, the corresponding section
 * is set to {@code null} and the failure reason is recorded in the {@code errors} list,
 * allowing the dashboard to render a partial payload rather than failing entirely.
 */
@ApplicationScoped
public class GetDashboardPayloadUseCase {

    private static final Logger LOG = Logger.getLogger(GetDashboardPayloadUseCase.class.getName());

    private final TicketMetricsProvider ticketMetricsProvider;
    private final SliSloMetricsProvider sliSloMetricsProvider;
    private final IncidentMetricsProvider incidentMetricsProvider;
    private final ChangeMetricsProvider changeMetricsProvider;

    @Inject
    public GetDashboardPayloadUseCase(TicketMetricsProvider ticketMetricsProvider,
                                      SliSloMetricsProvider sliSloMetricsProvider,
                                      IncidentMetricsProvider incidentMetricsProvider,
                                      ChangeMetricsProvider changeMetricsProvider) {
        this.ticketMetricsProvider = ticketMetricsProvider;
        this.sliSloMetricsProvider = sliSloMetricsProvider;
        this.incidentMetricsProvider = incidentMetricsProvider;
        this.changeMetricsProvider = changeMetricsProvider;
    }

    /**
     * Execute the dashboard payload aggregation.
     *
     * @return a {@link DashboardPayload} with all available metrics and any errors encountered
     */
    public DashboardPayload execute() {
        List<String> errors = new ArrayList<>();

        TicketsByState ticketsByState = fetchSafely(
                () -> ticketMetricsProvider.getTicketsByState(),
                "ticketsByState", errors);

        SliSloCompliance sliSloCompliance = fetchSafely(
                () -> sliSloMetricsProvider.getSliSloCompliance(),
                "sliSloCompliance", errors);

        IncidentMetrics incidentMetrics = fetchSafely(
                () -> incidentMetricsProvider.getIncidentMetrics(),
                "incidentMetrics", errors);

        ChangeManagement changeManagement = fetchSafely(
                () -> changeMetricsProvider.getChangeManagement(),
                "changeManagement", errors);

        ErrorBudget errorBudget = fetchSafely(
                () -> changeMetricsProvider.getErrorBudget(),
                "errorBudget", errors);

        ExecutiveSummary executiveSummary = buildExecutiveSummary(
                ticketsByState, sliSloCompliance, errorBudget);

        return new DashboardPayload(
                executiveSummary,
                ticketsByState,
                sliSloCompliance,
                incidentMetrics,
                errorBudget,
                changeManagement,
                List.copyOf(errors)
        );
    }

    private <T> T fetchSafely(java.util.function.Supplier<T> supplier, String section, List<String> errors) {
        try {
            return supplier.get();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to fetch " + section, e);
            errors.add("Failed to fetch " + section + ": " + e.getMessage());
            return null;
        }
    }

    private ExecutiveSummary buildExecutiveSummary(TicketsByState tickets,
                                                   SliSloCompliance sliSlo,
                                                   ErrorBudget errorBudget) {
        int totalOpenTickets = tickets != null
                ? tickets.newCount() + tickets.processingAssigned()
                  + tickets.processingPlanned() + tickets.pending()
                : 0;

        // Critical incidents default to 0; a real implementation would derive this from incident data
        int criticalIncidents = 0;

        double overallAvailability = sliSlo != null ? sliSlo.availabilitySli() : 0.0;

        double errorBudgetRemaining = errorBudget != null ? errorBudget.remainingPercentage() : 0.0;

        return new ExecutiveSummary(totalOpenTickets, criticalIncidents, overallAvailability, errorBudgetRemaining);
    }
}
