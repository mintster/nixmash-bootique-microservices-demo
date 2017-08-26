package com.nixmash.postservice.resource;

import com.google.inject.Inject;
import com.nixmash.jangles.json.JanglesUser;
import com.nixmash.postservice.service.PostServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
public class PostResource extends Application {

	private final PostServiceImpl postService;

	@Inject
	public PostResource(PostServiceImpl postService) {
		this.postService = postService;
	}

	@Path("/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JanglesUser get(@PathParam("userId") long userId) {
		return  postService.getJanglesUser(userId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<JanglesUser> getAll() {
		return postService.getJanglesUsers();
	}
}
