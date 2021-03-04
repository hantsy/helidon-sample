package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.ok;

@RequestScoped
public class CommentResource {
    private final static Logger LOGGER = Logger.getLogger(CommentResource.class.getName());

    private final CommentRepository comments;

    @PathParam("id")
    String postId;

    @Inject
    public CommentResource(CommentRepository commentRepository) {
        this.comments = commentRepository;
    }

    @GET
    public Response getAllComments() {
        return ok(this.comments.findByPostId(this.postId)).build();
    }

    @POST
    public Response saveComment(@Valid CommentForm commentForm, UriInfo uriInfo) {
        var saved = this.comments.save(Comment.of(this.postId, commentForm.getContent()));
        var createdUri = uriInfo.getBaseUriBuilder()
                .path("/posts/{id}/comments/{commentId}")
                .build(this.postId, saved.getId());
        return created(createdUri).build();
    }
}
