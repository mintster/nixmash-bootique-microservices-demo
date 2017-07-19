package com.nixmash.web.rest;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.web.controller.GeneralController;
import com.nixmash.web.guice.WebTestModule;
import com.nixmash.web.resolvers.TemplatePathResolver;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static com.nixmash.web.utils.TestUtils.TEST_URL;
import static com.nixmash.web.utils.TestUtils.YAML_CONFIG;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by daveburke on 7/1/17.
 */
@RunWith(JUnit4.class)
public class ControllerTest {


    @ClassRule
    public static JettyTestFactory JETTY_FACTORY = new JettyTestFactory();

    @Inject
    private GeneralController mockGeneralController;

    @Inject
    private TemplatePathResolver templatePathResolver;

    private Client client;

    private Answer<String> usersAnswer = new Answer<String>() {
        public String answer(InvocationOnMock invocation) throws Throwable {
            Map<String, Object> model = new HashMap<>();
            model.put("users", new ArrayList<>());
            return templatePathResolver.populateTemplate("users.html", model);
        }
    };

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
                .module(binder -> JerseyModule.extend(binder).addResource(GeneralController.class))
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

        this.mockGeneralController = Mockito.mock(GeneralController.class);
        when(mockGeneralController.restUsers()).thenAnswer(usersAnswer);
    }

    /**
     *
     *  Normal access to "/users" will display the error.html page
     */
    @Test
    public void getUsersTest() throws Exception {
        WebTarget target = client.target(TEST_URL + "/users");
        Response response = target.request().get();
        assertTrue(response.readEntity(String.class).contains("Oops!"));
    }

    /**
     *
     * GeneralController is mocked so method returns Stubbed Answer
     *  displaying users.html page
     */
    @Test
    public void usersPageDisplays() throws Exception {
        String populatedTemplate = mockGeneralController.restUsers();
        assertTrue(populatedTemplate.contains("<title>Users Test Page</title>"));
    }

}
