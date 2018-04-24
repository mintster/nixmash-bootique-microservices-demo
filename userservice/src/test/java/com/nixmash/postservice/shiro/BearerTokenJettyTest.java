package com.nixmash.postservice.shiro;

import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.auth.BearerTokenRealm;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.dto.BearerTokenKey;
import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.enums.JanglesAppId;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;
import com.nixmash.postservice.guice.TestConnection;
import io.bootique.BQRuntime;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.shiro.ShiroModule;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static org.junit.Assert.*;

/**
 * Created by daveburke on 7/6/17.
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@RunWith(JUnit4.class)
public class BearerTokenJettyTest {

    private static final String BOB = "bob";
    private static final String TEST_URL = "http://localhost:8001";

    @ClassRule
    public static JettyTestFactory TEST_SERVER = new JettyTestFactory();

    private static BQRuntime runtime;

    private static UserService userService;
    private static JanglesGlobals janglesGlobals;

    @BeforeClass
    public static void beforeClass() {
        runtime = TEST_SERVER.app()
                .args("--config=classpath:test.yml")
                .autoLoadModules()
                .module(b -> b.bind(IConnection.class).to(TestConnection.class))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> b.bind(JanglesGlobals.class))
                .module(binder -> JerseyModule.extend(binder).addResource(RestApi.class))
                .module(b -> ShiroModule.extend(b).addRealm(BearerTokenRealm.class))
                .start();

        janglesGlobals = runtime.getInstance(JanglesGlobals.class);
        userService = runtime.getInstance(UserService.class);

        try {
            configureTestDb("populate.sql");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        DefaultSecurityManager sm = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(sm);
        ThreadContext.bind(sm);
    }


    @Test
    public void passTokenTest() {
        User bob = userService.getUser(BOB);
        BearerAuthenticationToken token = userService.createBearerToken(new CurrentUser(bob), JanglesAppId.USER_SERVICE);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/pass_token/" + token.getToken()).request().get();
        assertEquals(r1.readEntity(String.class), bob.getUserKey());
    }

    @Test
    public void authWithTokenTest() {
        User bob = userService.getUser(BOB);
        BearerAuthenticationToken token = userService.createBearerToken(new CurrentUser(bob), JanglesAppId.USER_SERVICE);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/auth_with_token/" + token.getToken()).request().get();
        assertTrue(r1.readEntity(Boolean.class));
    }

    @Test
    public void authWithTokenBadApiKeyTest() {
        User bob = userService.getUser(BOB);
        BearerAuthenticationToken token = userService.createBearerToken(new CurrentUser(bob), null);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/auth_with_token/" + token.getToken()).request().get();
        assertFalse(r1.readEntity(Boolean.class));
    }

    @Test
    public void authWithAnonymousTokenKeyTest() {
        BearerAuthenticationToken token = userService.createAnonymousToken();
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/auth_with_token/" + token.getToken()).request().get();
        assertFalse(r1.readEntity(Boolean.class));
    }

    @Test
    public void authWithNoTokenKeyTest() {
        User bob = userService.getUser(BOB);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/auth_with_token/").request().get();
        assertTrue(r1.readEntity(Boolean.class));
    }

    @Test
    public void helloTest()   {
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/hello").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r1.getStatus());
    }

    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static class RestApi {

        @GET
        @Path("/hello")
        public String hello() {
            return "hello";
        }

        @GET
        @Path("/pass_token/{userToken}")
        @Produces(MediaType.TEXT_PLAIN)
        public String passTokenKey(@PathParam("userToken") BearerAuthenticationToken token) {
            BearerTokenKey bearerTokenKey = userService.decodeBearerToken(token);
            return bearerTokenKey.getUserkey();
        }

        @GET
        @Path("/auth_with_token/{userToken}")
        @Produces(MediaType.TEXT_PLAIN)
        public Boolean authWithTokenKey(@PathParam("userToken") BearerAuthenticationToken token) {
            try {
                Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
                subject.login(token);
                subject.checkRole("admin");
                subject.logout();
                return true;
            } catch (AuthenticationException e) {
                return false;
            }
        }

        @GET
        @Path("/auth_with_token/")
        @Produces(MediaType.TEXT_PLAIN)
        public Boolean authWithNoTokenKey() {
                return true;
        }
    }


}
