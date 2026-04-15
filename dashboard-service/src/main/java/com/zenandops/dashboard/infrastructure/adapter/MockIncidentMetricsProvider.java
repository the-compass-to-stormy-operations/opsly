package com.zenandops.dashboard.infrastructure.adapter;

import com.zenandops.dashboard.application.port.IncidentMetricsProvider;
import com.zenandops.dashboard.domain.valueobject.IncidentMetrics;
import com.zenandops.dashboard.domain.valueobject.Trend;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mock implementation of {@link IncidentMetricsProvider} returning realistic
 * MTTR and MTTD values with trend indicators.
 */
@ApplicationScoped
public class MockIncidentMetricsProvider implements IncidentMetricsProvider {

    @Override
    public IncidentMetrics getIncidentMetrics() {
        return new IncidentMetrics(47.3, Trend.DOWN, 8.2, Trend.STABLE);
    }
}
