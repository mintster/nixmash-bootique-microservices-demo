package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.resolvers.TemplatePathResolver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class GeneralController {

    // region Constants

    private static final String HOME_PAGE = "home";
    private static final String USERS_PAGE = "users";
    private static final String JUMBOTRON_TITLE_KEY = "jumbotron.title";
    private static final String JUMBOTRON_SUBTITLE_KEY = "jumbotron.subtitle";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public GeneralController(TemplatePathResolver templatePathResolver, WebUI webUI) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
    }

    // endregion


    @GET
    public String home() {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(HOME_PAGE));
        return templatePathResolver.populateTemplate("home.html", model);
    }

}
