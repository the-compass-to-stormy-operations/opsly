package com.zenandops.dashboard.application.port;

import com.zenandops.dashboard.domain.valueobject.IncidentMetrics;

/**
 * Outbound port for retrieving incident response metrics (MTTR, MTTD) with trend indicators.
 */
public interface IncidentMetricsProvider {

    IncidentMetrics getIncidentMetrics();
}
