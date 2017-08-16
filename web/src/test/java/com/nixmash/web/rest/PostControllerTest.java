package com.nixmash.web.rest;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.db.cn.MySqlConnection;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;
import com.nixmash.web.auth.NixmashRealm;
import com.nixmash.web.controller.PostController;
import com.nixmash.web.guice.GuiceJUnit4Runner;
import com.nixmash.web.guice.WebTestModule;
import com.nixmash.web.resolvers.TemplatePathResolver;
import io.bootique.BQRuntime;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.shiro.ShiroModule;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static com.nixmash.web.utils.TestUtils.TEST_URL;
import static com.nixmash.web.utils.TestUtils.YAML_CONFIG;
import static org.junit.Assert.assertTrue;

/**
 * Created by daveburke on 7/1/17.
 */
@RunWith(GuiceJUnit4Runner.class)
public class PostControllerTest {

    @ClassRule
    public static JettyTestFactory testFactory = new JettyTestFactory();

    @Inject
    private TemplatePathResolver templatePathResolver;

    @Inject
    private static UserService userService;

    private Client client;

    private static BQRuntime runtime;

    private static DefaultSecurityManager sm;

    @BeforeClass
    public static void setupClass() {
        try {
            configureTestDb("populate.sql");
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Package pkg = PostController.class.getPackage();
        runtime = testFactory.app()
                .autoLoadModules()
                .args(YAML_CONFIG)
                .module(binder -> JerseyModule.extend(binder).addPackage(pkg))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> b.bind(IConnection.class).to(MySqlConnection.class))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .start();

        userService = runtime.getInstance(UserService.class);

        sm = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(sm);
        ThreadContext.bind(sm);
    }

    @Before
    public void startJetty() {
        Injector injector = Guice.createInjector(new WebTestModule());
        injector.injectMembers(this);

        ClientConfig config = new ClientConfig();
        this.client = ClientBuilder.newClient(config);
    }

    /**
     *
     *  "/posts" will display the posts.html page
     */
    @Test
    public void getPostsPageTest() throws Exception {

        // TODO: authenticate user for jetty tests. i.e., next 2 lines have no purpose
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));

        WebTarget target = client.target(TEST_URL + "/posts");
        Response response = target.request().get();
        assertTrue(response.readEntity(String.class).contains("<meta name='page_key' content='login'/>"));
    }

}
