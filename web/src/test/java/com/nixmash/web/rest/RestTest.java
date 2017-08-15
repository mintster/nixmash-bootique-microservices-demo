package com.nixmash.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nixmash.jangles.model.JanglesUser;
import com.nixmash.web.exceptions.RestProcessingException;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by daveburke on 7/6/17.
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@RunWith(JUnit4.class)
public class RestTest {

    @ClassRule
    public static JettyTestFactory TEST_SERVER = new JettyTestFactory();

    @BeforeClass
    public static void beforeClass() {
        TEST_SERVER.app()
                .args("--config=classpath:bootique-tests.yml")
                .autoLoadModules()
                .module(binder -> JerseyModule.extend(binder).addResource(RestApi.class))
//                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
//                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
//                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .start();
    }

    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static class RestApi {

        @GET
        @Path("/users")
        public String getUsers() {
            return "[\n" +
                    "{\n" +
                    "\"userId\": 1,\n" +
                    "\"userName\": \"Jim\",\n" +
                    "\"displayName\": \"Jim Johnson\",\n" +
                    "\"dateCreated\": \"06-23-2017 09:01:25\",\n" +
                    "\"isActive\": true,\n" +
                    "\"link\": \"http://localhost:8000/users/1\"\n" +
                    "}\n" +
                    "]";
        }

        @GET
        @Path("/hello")
        public String hello() {
            return "hello";
        }
    }

    @Test
    public void getRestUsersTest() throws RestProcessingException {
        List<JanglesUser> users = (List<JanglesUser>) getRestList("/users", JanglesUser.class);
        assertTrue(users.get(0).getUserName().equals("Jim"));
    }

    @Test(expected = RestProcessingException.class)
    public void badRestUsersUrlTest() throws RestProcessingException {
        List<JanglesUser> users = (List<JanglesUser>) getRestList("/badurl", JanglesUser.class);
    }

    @Test
    public void helloTest() throws RestProcessingException {
        WebTarget base = ClientBuilder.newClient().target("http://localhost:9001");
        Response r1 = base.path("/hello").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r1.getStatus());
    }

    private List<?> getRestList(String path, Class clazz) throws RestProcessingException {

        Response response = null;
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = null;
        WebTarget base = ClientBuilder.newClient().target("http://localhost:9001");

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

}
