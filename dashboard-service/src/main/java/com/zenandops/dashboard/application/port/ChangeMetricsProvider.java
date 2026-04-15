package com.zenandops.dashboard.application.port;

import com.zenandops.dashboard.domain.valueobject.ChangeManagement;
import com.zenandops.dashboard.domain.valueobject.ErrorBudget;

/**
 * Outbound port for retrieving change management metrics and error budget consumption.
 */
public interface ChangeMetricsProvider {

    ChangeManagement getChangeManagement();

    ErrorBudget getErrorBudget();
}
