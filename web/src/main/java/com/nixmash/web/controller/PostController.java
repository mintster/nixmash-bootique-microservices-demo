package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.resolvers.TemplatePathResolver;
import com.nixmash.web.service.UserClientService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/posts")
public class PostController {

    // region Constants

    private static final String POSTS_PAGE = "posts";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final UserClientService userClientService;

    @Inject
    public PostController(TemplatePathResolver templatePathResolver,
                          WebUI webUI, UserClientService userClientService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.userClientService = userClientService;
    }

    // endregion


    @GET
    public String getPostsPage() {
        Map<String, Object> model = webUI.getBasePageInfo(POSTS_PAGE);
        return templatePathResolver.populateTemplate("posts.html", model);
    }

}
