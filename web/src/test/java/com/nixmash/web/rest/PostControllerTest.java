package com.nixmash.web.rest;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.web.controller.PostController;
import com.nixmash.web.guice.WebTestModule;
import com.nixmash.web.resolvers.TemplatePathResolver;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
@RunWith(JUnit4.class)
public class PostControllerTest {

    @ClassRule
    public static JettyTestFactory JETTY_FACTORY = new JettyTestFactory();

    @Inject
    private TemplatePathResolver templatePathResolver;

    private Client client;

    @BeforeClass
    public static void setupClass() {
        try {
            configureTestDb("populate.sql");
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        JETTY_FACTORY.app()
                .autoLoadModules()
                .args(YAML_CONFIG)
                .module(binder -> JerseyModule.extend(binder).addResource(PostController.class))
                .start();

    }

    @AfterClass
    public static void tearDown() {
        try {
            configureTestDb("clear.sql");
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
        WebTarget target = client.target(TEST_URL + "/posts");
        Response response = target.request().get();
        assertTrue(response.readEntity(String.class).contains("<h1>Posts</h1>"));
    }


}
