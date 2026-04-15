package com.zenandops.dashboard.domain.valueobject;

/**
 * Error budget consumption data within an SLO window.
 */
public record ErrorBudget(
        double remainingPercentage,
        double burnRate,
        int windowDays
) {
}
