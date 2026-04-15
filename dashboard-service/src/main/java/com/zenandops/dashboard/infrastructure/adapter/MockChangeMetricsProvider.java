package com.zenandops.dashboard.infrastructure.adapter;

import com.zenandops.dashboard.application.port.ChangeMetricsProvider;
import com.zenandops.dashboard.domain.valueobject.ChangeManagement;
import com.zenandops.dashboard.domain.valueobject.ErrorBudget;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mock implementation of {@link ChangeMetricsProvider} returning realistic
 * change failure rate and error budget consumption data.
 */
@ApplicationScoped
public class MockChangeMetricsProvider implements ChangeMetricsProvider {

    @Override
    public ChangeManagement getChangeManagement() {
        return new ChangeManagement(4.8, 62, 3);
    }

    @Override
    public ErrorBudget getErrorBudget() {
        return new ErrorBudget(68.5, 1.2, 30);
    }
}
