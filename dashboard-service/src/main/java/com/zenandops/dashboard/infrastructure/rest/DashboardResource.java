package com.zenandops.dashboard.infrastructure.rest;

import com.zenandops.dashboard.application.usecase.GetDashboardPayloadUseCase;
import com.zenandops.dashboard.domain.valueobject.DashboardPayload;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource exposing the operational dashboard endpoint.
 * Requires a valid Access_Token (JWT) for access.
 */
@Path("/api/v1/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class DashboardResource {

    @Inject
    GetDashboardPayloadUseCase getDashboardPayloadUseCase;

    @GET
    public DashboardPayload getDashboard() {
        return getDashboardPayloadUseCase.execute();
    }
}
