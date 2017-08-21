/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.nixmash.web.controller;

import com.google.inject.Inject;
import com.nixmash.jangles.service.UserService;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.resolvers.TemplatePathResolver;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.nixmash.jangles.service.UserServiceImpl.CURRENT_USER;

@Path("/login")
public class LoginController {

    private static transient final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String LOGIN_PAGE = "login";

    private UserService userService;
    private final TemplatePathResolver templatePathResolver;
    private final WebUI webUI;

    @Inject
    public LoginController(TemplatePathResolver templatePathResolver,
                          WebUI webUI, UserService userService) {
        this.templatePathResolver = templatePathResolver;
        this.webUI = webUI;
        this.userService = userService;
    }

    @GET
    public String loginPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", webUI.getPageInfo(LOGIN_PAGE));
        return templatePathResolver.populateTemplate("login.html", model);
    }

    @POST
    public Response onSubmit(@FormParam("username") String username,
                             @FormParam("password") String password) throws Exception {

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            subject.getSession().setAttribute(CURRENT_USER, userService.getCurrentUser(subject));
        } catch (AuthenticationException e) {
            log.debug("Error authenticating.", e);
            URI targetURIForRedirection = new URI("/login");
            return Response.seeOther(targetURIForRedirection).build();
        }

        URI targetURIForRedirection = new URI("/");
        return Response.seeOther(targetURIForRedirection).build();
    }
}