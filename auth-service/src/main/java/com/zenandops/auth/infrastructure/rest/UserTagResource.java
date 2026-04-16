package com.zenandops.auth.infrastructure.rest;

import com.zenandops.auth.application.usecase.AssignTagsToUserUseCase;
import com.zenandops.auth.application.usecase.GetUserTagsUseCase;
import com.zenandops.auth.application.usecase.RemoveTagsFromUserUseCase;
import com.zenandops.auth.domain.entity.Tag;
import com.zenandops.auth.infrastructure.rest.dto.TagResponse;
import com.zenandops.auth.infrastructure.rest.dto.UserTagsRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST resource exposing User-Tag assignment endpoints. All endpoints require ADMIN role.
 */
@Path("/api/v1/users/{userId}/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserTagResource {

    @Inject
    GetUserTagsUseCase getUserTagsUseCase;

    @Inject
    AssignTagsToUserUseCase assignTagsToUserUseCase;

    @Inject
    RemoveTagsFromUserUseCase removeTagsFromUserUseCase;

    @GET
    public Response getUserTags(@PathParam("userId") String userId) {
        List<Tag> tags = getUserTagsUseCase.execute(userId);
        List<TagResponse> response = tags.stream()
                .map(this::toResponse)
                .toList();
        return Response.ok(response).build();
    }

    @POST
    public Response assignTags(@PathParam("userId") String userId, UserTagsRequest request) {
        assignTagsToUserUseCase.execute(userId, request.tagIds());
        List<Tag> tags = getUserTagsUseCase.execute(userId);
        List<TagResponse> response = tags.stream()
                .map(this::toResponse)
                .toList();
        return Response.ok(response).build();
    }

    @DELETE
    public Response removeTags(@PathParam("userId") String userId, UserTagsRequest request) {
        removeTagsFromUserUseCase.execute(userId, request.tagIds());
        List<Tag> tags = getUserTagsUseCase.execute(userId);
        List<TagResponse> response = tags.stream()
                .map(this::toResponse)
                .toList();
        return Response.ok(response).build();
    }

    private TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getKey(), tag.getValue(),
                tag.getDescription(), tag.getCreatedAt(), tag.getUpdatedAt());
    }
}
