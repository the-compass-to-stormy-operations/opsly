package com.zenandops.dashboard.infrastructure.adapter;

import com.zenandops.dashboard.application.port.SliSloMetricsProvider;
import com.zenandops.dashboard.domain.valueobject.SliSloCompliance;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mock implementation of {@link SliSloMetricsProvider} returning realistic
 * SRE metrics for availability and latency SLI/SLO compliance.
 */
@ApplicationScoped
public class MockSliSloMetricsProvider implements SliSloMetricsProvider {

    @Override
    public SliSloCompliance getSliSloCompliance() {
        return new SliSloCompliance(99.92, 99.9, 96.5, 95.0);
    }
}
