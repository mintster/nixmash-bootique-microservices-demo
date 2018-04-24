package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.resolvers.TemplatePathResolver;
import com.nixmash.web.service.UserClientService;
import org.apache.shiro.SecurityUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final UserClientService userClientService;

    @Inject
    public GeneralController(TemplatePathResolver templatePathResolver, WebUI webUI, UserClientService userClientService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.userClientService = userClientService;
    }

    // endregion


    @GET
    public String home() {
        Map<String, Object> model = webUI.getBasePageInfo(HOME_PAGE);
        return templatePathResolver.populateTemplate("home.html", model);
    }

    @GET
    @Path("/login.jsp")
    public Response redirectLogin() throws URISyntaxException {
        URI targetURIForRedirection = new URI("/login");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @GET
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        SecurityUtils.getSubject().logout();
        URI targetURIForRedirection = new URI("/?logout=true");
        return Response.temporaryRedirect(targetURIForRedirection).build();
    }

    @GET
    @Path("/tokencheck")
    public String authorizationCallback() {
        String tokenCheck = userClientService.performTokenCheck();
        Map<String, Object> model = webUI.getBasePageInfo(HOME_PAGE);
        model.put("tokenCheck", tokenCheck);
        return templatePathResolver.populateTemplate("home.html", model);
    }

}
