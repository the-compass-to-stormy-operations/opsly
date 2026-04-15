package com.zenandops.dashboard.domain.valueobject;

/**
 * SLI/SLO compliance percentages for availability and latency.
 */
public record SliSloCompliance(
        double availabilitySli,
        double availabilitySlo,
        double latencySli,
        double latencySlo
) {
}
