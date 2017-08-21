package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.jangles.json.JanglesUser;
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
@Path("/users")
public class UserController {

    // region Constants

    private static final String USERS_PAGE = "users";

    // endregion

    // region Constructor

    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;
    private final UserClientService userClientService;

    @Inject
    public UserController(TemplatePathResolver templatePathResolver,
                          WebUI webUI, UserClientService userClientService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.userClientService = userClientService;
    }

    // endregion


    @GET
    public String restUsers() {

        List<JanglesUser> users = null;
        try {
            users = userClientService.getRestUsers();
        } catch (RestProcessingException e) {
            return webUI.errorPage(e);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(USERS_PAGE));
        model.put("users", users);
        return templatePathResolver.populateTemplate("users.html", model);
    }

}
