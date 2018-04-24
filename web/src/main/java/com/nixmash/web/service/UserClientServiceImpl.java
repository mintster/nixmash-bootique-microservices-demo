package com.nixmash.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.enums.JanglesAppId;
import com.nixmash.jangles.json.JanglesUser;
import com.nixmash.jangles.service.UserService;
import com.nixmash.web.exceptions.RestProcessingException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static com.nixmash.jangles.service.UserServiceImpl.CURRENT_USER;

/**
 * Created by daveburke on 7/6/17.
 */
@SuppressWarnings("SameParameterValue")
public class UserClientServiceImpl implements UserClientService {

    private final JanglesCache janglesCache;
    private final JanglesGlobals janglesGlobals;
    private final UserService userService;

    @Inject
    public UserClientServiceImpl(JanglesCache janglesCache, JanglesGlobals janglesGlobals, UserService userService) {
        this.janglesCache = janglesCache;
        this.janglesGlobals = janglesGlobals;
        this.userService = userService;
    }

    // region secure methods

    @Override
    public String performTokenCheck() {
        String result = null;
        try {
            BearerAuthenticationToken bearerAuthenticationToken = getBearerAuthenticationToken();
            result = getRestTokenCheck(String.format("/token/%s", bearerAuthenticationToken.getToken()));
        } catch (RestProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private BearerAuthenticationToken getBearerAuthenticationToken() {
        BearerAuthenticationToken bearerAuthenticationToken = userService.createAnonymousToken();
        Session session = SecurityUtils.getSubject().getSession();
        if (SecurityUtils.getSubject().getPrincipals() != null) {
            CurrentUser currentUser = (CurrentUser) session.getAttribute(CURRENT_USER);
            bearerAuthenticationToken = userService.createBearerToken(currentUser, JanglesAppId.WEB_CLIENT);
        }
        return bearerAuthenticationToken;
    }

    // endregion

    // region public override methods

    @SuppressWarnings("unchecked")
    @Override
    public List<JanglesUser> getRestUsers() throws RestProcessingException {

        String key = janglesRestUsersCacheKey();
        List<JanglesUser> users = (List<JanglesUser>) janglesCache.get(key);
        try {
            if (users == null) {
                users = (List<JanglesUser>) getRestList("/users", JanglesUser.class);
                janglesCache.put(key, (Serializable) users);
            }
        } catch (RestProcessingException e) {
            throw new RestProcessingException(e.getMsg());
        }
        return users;
    }

    // endregion

    // region Private Methods

    // endregion

    // region Rest Actions

    protected String getRestTokenCheck(String path)
            throws RestProcessingException {


        String message;
        WebTarget base = ClientBuilder.newClient().target(janglesGlobals.userServiceUrl);
        Response response = base.path(path).request().get();
        message = response.readEntity(String.class);
        response.close();
        return message;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getRestObject(Class clazz, String path) throws RestProcessingException {

        Object object;
        Response response;
        ObjectMapper mapper = new ObjectMapper();
        WebTarget base = ClientBuilder.newClient().target(janglesGlobals.userServiceUrl);

        try {
            response = base.path(path).request().get();
        } catch (ProcessingException e) {
            throw new RestProcessingException(e.getMessage());
        }

        try {
            object = mapper.readValue(response.readEntity(String.class),
                    TypeFactory
                            .defaultInstance()
                            .constructType(clazz));
        } catch (IOException e) {
            response.close();
            throw new RestProcessingException(e.getMessage());
        }
        response.close();
        return (T)object;
    }

    private List<?> getRestList(String path, Class clazz) throws RestProcessingException {

        Response response = null;
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = null;
        WebTarget base = ClientBuilder.newClient().target(janglesGlobals.userServiceUrl);

        try {
            response = base.path(path).request().get();
        } catch (ProcessingException e) {
            throw new RestProcessingException(e.getMessage());
        }

        try {
            list = mapper.readValue(response.readEntity(String.class),
                    TypeFactory
                            .defaultInstance()
                            .constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            response.close();
            throw new RestProcessingException(e.getMessage());
        }
        response.close();
        return list;
    }

    // endregion

    // region CacheKeys

    public String janglesRestUsersCacheKey() {
        return String.format("JanglesRestUsers-%s", janglesGlobals.cloudApplicationId);
    }

    // endregion

}
