package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.model.JanglesUser;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.exceptions.RestProcessingException;
import com.nixmash.web.resolvers.TemplatePathResolver;
import com.nixmash.web.service.UserClientService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daveburke on 6/26/17.
 */
@SuppressWarnings("ALL")
@Path("/")
public class GeneralController {

    // region Constants

    private static final String HOME_PAGE = "home";
    private static final String USERS_PAGE = "users";
    private static final String ERROR_PAGE = "error";
    private static final String JUMBOTRON_TITLE_KEY = "jumbotron.title";
    private static final String JUMBOTRON_SUBTITLE_KEY = "jumbotron.subtitle";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final JanglesCache janglesCache;
    private final UserClientService userClientService;

    @Inject
    public GeneralController(TemplatePathResolver templatePathResolver,
                             WebUI webUI, JanglesCache janglesCache, UserClientService userClientService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.janglesCache = janglesCache;
        this.userClientService = userClientService;
    }

    // endregion


    @GET
    public String home() {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(HOME_PAGE));
        model.put("trans", webUI.getResourceBundle());
        model.put("jumbotronTitle", webUI.getMessage(JUMBOTRON_TITLE_KEY));
        model.put("jumbotronSubtitle", webUI.getMessage(JUMBOTRON_SUBTITLE_KEY));
        return templatePathResolver.populateTemplate("home.html", model);
    }

    @GET
    @Path("/users")
    public String restUsers() {

        List<JanglesUser> users = null;
        try {
            users = userClientService.getRestUsers();
        } catch (RestProcessingException e) {
            return errorPage(e);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(USERS_PAGE));
        model.put("trans", webUI.getResourceBundle());
        model.put("users", users);
        model.put("usersActive", " class=active");
        return templatePathResolver.populateTemplate("users.html", model);
    }

    private String errorPage(RestProcessingException e) {
        Map<String, Object> model = new HashMap<>();
        model.put("trans", webUI.getResourceBundle());
        model.put("pageinfo", webUI.getPageInfo(ERROR_PAGE));
        model.put("errorMessage", e.getMsg());
        return templatePathResolver.populateTemplate("error.html", model);
    }
}
