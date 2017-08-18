package com.nixmash.web.rest;

import com.google.common.io.BaseEncoding;
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
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.util.ThreadState;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
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
import java.util.concurrent.atomic.AtomicReference;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static com.nixmash.web.utils.TestUtils.TEST_URL;
import static com.nixmash.web.utils.TestUtils.YAML_CONFIG;
import static org.junit.Assert.assertTrue;

/**
 * Created by daveburke on 7/1/17.
 */
@RunWith(GuiceJUnit4Runner.class)
public class PostControllerTest {

    BasicHttpAuthenticationFilter testFilter;

    @ClassRule
    public static JettyTestFactory testFactory = new JettyTestFactory();

    @Inject
    private TemplatePathResolver templatePathResolver;

    @Inject
    private static UserService userService;

    private Client client;

    private static BQRuntime runtime;

    private static DefaultSecurityManager sm;
    protected AtomicReference<String> sessionCookie = new AtomicReference<>();
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
     * "/posts" should display the posts.html page w/authenticated user but does not...
     */
    @Test
    public void getPostsPageTest() throws Exception {
        // TODO: this is shit
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));

        // TODO: this too
        ThreadState threadState = new SubjectThreadState(subject);
        threadState.bind();

        Session session = subject.getSession();

        // TODO: this too
        subject.execute(new Runnable() {
            public void run() {
                WebTarget target = client.target(TEST_URL + "/posts")
                        .property("Content-Type", "application/x-www-form-urlencoded")
                        .property("Authorization", "Basic " + BaseEncoding.base64().encode("bob:password".getBytes()))
                        .property("Host", "localhost");
                Response response = target.request()
                        .header("Authorization", createAuthorizationHeader("bob", "password"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Host","localhost")
                        .get();
                assertTrue(response.readEntity(String.class).contains("<meta name='page_key' content='login'/>"));
            }
        });

        // TODO: and this
        HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic("bob", "password");
        client.register(auth);

        // TODO: these redirect to /login with no awareness of Shiro Principal
        WebTarget target = client.target(TEST_URL + "/posts");
        Response response = target.request()
                .header("Authorization", createAuthorizationHeader("bob", "password"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Host","localhost").get();
        assertTrue(response.readEntity(String.class).contains("<meta name='page_key' content='login'/>"));

    }

    private String createAuthorizationHeader(String username, String password) {
        return "Basic " + new String(Base64.encode((username + ":" + password).getBytes()));
    }
}
