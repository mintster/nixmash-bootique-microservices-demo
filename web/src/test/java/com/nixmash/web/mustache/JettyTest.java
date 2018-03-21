package com.nixmash.web.mustache;

import com.google.inject.Inject;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;
import com.nixmash.web.auth.NixmashRealm;
import com.nixmash.web.controller.GeneralController;
import com.nixmash.web.guice.GuiceJUnit4Runner;
import com.nixmash.web.guice.TestConnection;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.shiro.ShiroModule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertTrue;

@RunWith(GuiceJUnit4Runner.class)
public class JettyTest {

    @ClassRule
    public static JettyTestFactory TEST_SERVER = new JettyTestFactory();

    @Inject
    private static UserService userService;

    @BeforeClass
    public static void beforeClass() {

        Package pkg = GeneralController.class.getPackage();
        TEST_SERVER.app()
                .args("--config=classpath:test.yml")
                .autoLoadModules()
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> b.bind(IConnection.class).to(TestConnection.class))
                .module(binder -> JerseyModule.extend(binder).addPackage(pkg))
                .module(b -> ShiroModule.extend(b).addRealm(new NixmashRealm(userService)))
                .start();
    }

    @Test
    public void homePageTest() {
        assertTrue(responseOK(pathResponse("/")));
    }

    @Test
    public void usersPageTest() {
        assertTrue(responseOK(pathResponse("/users")));
    }

    @Test
    public void loginPageTest() {
        assertTrue(responseOK(pathResponse("/login")));
    }

    private Response pathResponse(String path) {
        WebTarget base = ClientBuilder.newClient().target("http://localhost:9001");
        return base.path(path).request().get();
    }

    private Boolean responseOK(Response response) {
        return Status.OK.getStatusCode() == response.getStatus();
    }
}
