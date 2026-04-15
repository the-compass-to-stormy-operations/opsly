package com.zenandops.dashboard.domain.valueobject;

/**
 * Incident response metrics with trend indicators.
 */
public record IncidentMetrics(
        double mttrMinutes,
        Trend mttrTrend,
        double mttdMinutes,
        Trend mttdTrend
) {
}
