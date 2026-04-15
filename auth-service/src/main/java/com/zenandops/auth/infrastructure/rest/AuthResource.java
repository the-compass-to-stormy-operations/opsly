package com.zenandops.auth.infrastructure.rest;

import com.zenandops.auth.application.usecase.LoginUseCase;
import com.zenandops.auth.application.usecase.LogoffUseCase;
import com.zenandops.auth.application.usecase.RefreshTokenUseCase;
import com.zenandops.auth.application.usecase.TokenPair;
import com.zenandops.auth.infrastructure.rest.dto.LoginRequest;
import com.zenandops.auth.infrastructure.rest.dto.RefreshRequest;
import com.zenandops.auth.infrastructure.rest.dto.TokenResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource exposing authentication endpoints.
 */
@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    RefreshTokenUseCase refreshTokenUseCase;

    @Inject
    LogoffUseCase logoffUseCase;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        TokenPair tokenPair = loginUseCase.execute(request.login(), request.password());
        return Response.ok(new TokenResponse(tokenPair.accessToken(), tokenPair.refreshToken()))
                .build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(RefreshRequest request) {
        TokenPair tokenPair = refreshTokenUseCase.execute(request.refreshToken());
        return Response.ok(new TokenResponse(tokenPair.accessToken(), tokenPair.refreshToken()))
                .build();
    }

    @POST
    @Path("/logoff")
    @Authenticated
    public Response logoff(RefreshRequest request) {
        logoffUseCase.execute(request.refreshToken());
        return Response.noContent().build();
    }
}
