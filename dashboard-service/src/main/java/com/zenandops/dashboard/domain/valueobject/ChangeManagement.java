package com.zenandops.dashboard.domain.valueobject;

/**
 * Change management metrics including failure rate.
 */
public record ChangeManagement(
        double changeFailureRatePercentage,
        int totalChanges,
        int failedChanges
) {
}
