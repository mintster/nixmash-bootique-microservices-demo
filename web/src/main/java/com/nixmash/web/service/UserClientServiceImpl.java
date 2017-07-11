package com.nixmash.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.model.JanglesUser;
import com.nixmash.web.exceptions.RestProcessingException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by daveburke on 7/6/17.
 */
public class UserClientServiceImpl implements UserClientService {

    private final JanglesCache janglesCache;
    private final JanglesGlobals janglesGlobals;

    @Inject
    public UserClientServiceImpl(JanglesCache janglesCache, JanglesGlobals janglesGlobals) {
        this.janglesCache = janglesCache;
        this.janglesGlobals = janglesGlobals;
    }

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

    // region User Rest Actions

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
