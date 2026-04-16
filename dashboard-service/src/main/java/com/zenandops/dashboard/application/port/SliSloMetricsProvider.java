package com.zenandops.dashboard.application.port;

import com.zenandops.dashboard.domain.valueobject.SliSloCompliance;

/**
 * Outbound port for retrieving SLI/SLO compliance percentages.
 */
public interface SliSloMetricsProvider {

    SliSloCompliance getSliSloCompliance();
}
