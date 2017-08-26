package com.nixmash.postservice;

import com.nixmash.postservice.utils.JettyApp;
import io.bootique.jetty.JettyModule;
import io.bootique.jetty.MappedServlet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static com.nixmash.postservice.utils.TestUtils.TEST_URL;
import static com.nixmash.postservice.utils.TestUtils.YAML_CONFIG;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by daveburke on 6/25/17.
 */
@RunWith(JUnit4.class)
public class JettyTest {

    private Servlet mockServlet;

    @Rule
    public JettyApp app = new JettyApp();

    @Before
    public void before() {
        this.mockServlet = mock(Servlet.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMappedConfig() throws Exception {

        MappedServlet mappedServlet = new MappedServlet(mockServlet, new HashSet<>(Arrays.asList("/a/*", "/b/*")));
        app.start(binder -> JettyModule.extend(binder).addMappedServlet(mappedServlet));
        WebTarget base = ClientBuilder.newClient().target("http://localhost:8001");

        Response r1 = base.path("/a").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());

        Response r2 = base.path("/b").request().get();
        assertEquals(Status.OK.getStatusCode(), r2.getStatus());

        Response r3 = base.path("/c").request().get();
        assertEquals(Status.NOT_FOUND.getStatusCode(), r3.getStatus());

    }

    @Test
    public void getUsersTest() {
        app.start(binder -> JettyModule.extend(binder).addServlet(mockServlet, "servlet", "/*"), YAML_CONFIG);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/users").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
    }

    @Test
    public void testInitParametersPassed() {

        app.start(binder -> JettyModule.extend(binder).addServlet(new TestServlet(), "s1", "/*"), YAML_CONFIG);
        WebTarget base = ClientBuilder.newClient().target(TEST_URL);
        Response r1 = base.path("/").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("s1_a1_b2", r1.readEntity(String.class));
    }

    static class TestServlet extends HttpServlet {
        private static final long serialVersionUID = -3190255883516320766L;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("text/plain");

            ServletConfig config = getServletConfig();
            resp.getWriter().print(config.getServletName());
            resp.getWriter().print("_" + config.getServletContext().getInitParameter("a"));
            resp.getWriter().print("_" + config.getServletContext().getInitParameter("b"));
        }
    }
}