package com.zenandops.auth.infrastructure.rest;

import com.zenandops.auth.application.usecase.CreateTagUseCase;
import com.zenandops.auth.application.usecase.DeleteTagUseCase;
import com.zenandops.auth.application.usecase.GetTagUseCase;
import com.zenandops.auth.application.usecase.ListTagsUseCase;
import com.zenandops.auth.application.usecase.PaginatedResult;
import com.zenandops.auth.application.usecase.UpdateTagUseCase;
import com.zenandops.auth.domain.entity.Tag;
import com.zenandops.auth.infrastructure.rest.dto.CreateTagRequest;
import com.zenandops.auth.infrastructure.rest.dto.PaginatedTagsResponse;
import com.zenandops.auth.infrastructure.rest.dto.TagResponse;
import com.zenandops.auth.infrastructure.rest.dto.UpdateTagRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST resource exposing Tag CRUD endpoints. All endpoints require ADMIN role.
 */
@Path("/api/v1/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class TagResource {

    @Inject
    CreateTagUseCase createTagUseCase;

    @Inject
    ListTagsUseCase listTagsUseCase;

    @Inject
    GetTagUseCase getTagUseCase;

    @Inject
    UpdateTagUseCase updateTagUseCase;

    @Inject
    DeleteTagUseCase deleteTagUseCase;

    @POST
    public Response createTag(CreateTagRequest request) {
        Tag tag = createTagUseCase.execute(request.key(), request.value(), request.description());
        return Response.status(Response.Status.CREATED)
                .entity(toResponse(tag))
                .build();
    }

    @GET
    public Response listTags(@QueryParam("page") @DefaultValue("0") int page,
                             @QueryParam("size") @DefaultValue("20") int size) {
        PaginatedResult<Tag> result = listTagsUseCase.execute(page, size);
        List<TagResponse> items = result.items().stream()
                .map(this::toResponse)
                .toList();
        return Response.ok(new PaginatedTagsResponse(items, result.page(), result.size(),
                result.totalItems(), result.totalPages())).build();
    }

    @GET
    @Path("/{id}")
    public Response getTag(@PathParam("id") String id) {
        Tag tag = getTagUseCase.execute(id);
        return Response.ok(toResponse(tag)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTag(@PathParam("id") String id, UpdateTagRequest request) {
        Tag tag = updateTagUseCase.execute(id, request.description());
        return Response.ok(toResponse(tag)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") String id) {
        deleteTagUseCase.execute(id);
        return Response.noContent().build();
    }

    private TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getKey(), tag.getValue(),
                tag.getDescription(), tag.getCreatedAt(), tag.getUpdatedAt());
    }
}
