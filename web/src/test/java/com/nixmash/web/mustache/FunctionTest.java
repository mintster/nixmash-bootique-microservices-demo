package com.nixmash.web.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.functions.TranslateBundleFunction;
import com.github.mustachejava.resolver.ClasspathResolver;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.web.core.WebConfig;
import com.nixmash.web.core.WebContext;
import com.nixmash.web.core.WebGlobals;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.dto.PageInfo;
import com.nixmash.web.guice.WebTestModule;
import com.nixmash.web.resolvers.TemplatePathResolver;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.nixmash.web.utils.MustacheUtils.getContents;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by daveburke on 6/28/17.
 */
@RunWith(JUnit4.class)
public class FunctionTest {

    @Inject
    private WebContext webContext;

    @Inject
    private WebUI webUI;

    private WebContext mockContext;
    private WebGlobals mockGlobals;

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Before
    public void loadModuleTest() {
/*        BQTestRuntime runtime = testFactory.app("--server", "--config=classpath:bootique-tests.yml")
                .module(WebTestModule.class)
                .createRuntime();*/
        Injector injector = Guice.createInjector(new WebTestModule());
        injector.injectMembers(this);

        mockContext = mock(webContext.getClass());
        mockGlobals = new WebGlobals(new WebConfig());
        when(mockContext.globals()).thenReturn(mockGlobals);
        mockGlobals.cloudApplicationId = "webtest-Development";
    }


    private Boolean isInDevelopmentMode() {
        return mockContext
                .globals()
                .cloudApplicationId
                .toLowerCase()
                .contains("development");
    }

    @Test
    public void inDevelopmentModeTest() {
        assertNotNull(mockContext);

        // Testing isInDevelopmentMode Method Logic
        mockContext.globals().cloudApplicationId = "webtest-Development";
        assertTrue(isInDevelopmentMode());

        mockContext.globals().cloudApplicationId = "webtest";
        assertFalse(isInDevelopmentMode());
    }

    /*
    Global Cloud ApplicationId does not contain "Development" -- will not display PageInfo Metatags
     */
    @Test
    public void pageInfoInDevelopmentModeTest() {

        mockContext.globals().cloudApplicationId = "webtest";

        PageInfo pageinfo = PageInfo.getBuilder(1, "test", "test page")
                .heading("test heading")
                .subheading("test subheading")
                .inDevelopmentMode(isInDevelopmentMode())
                .build();

        StringWriter sw = populatePage("dto.html", pageinfo);
        assertFalse(sw.toString().contains("<meta name='page_id' content='1'/>"));
    }

    @Test
    public void pageInfoTests() throws IOException {

        PageInfo pageinfo = PageInfo.getBuilder(1, "test", "test page")
                .heading("test heading")
                .subheading("test subheading")
                .inDevelopmentMode(true)
                .build();
        StringWriter sw = populatePage("dto.html", pageinfo);
        assertTrue(sw.toString().contains("test heading"));
        assertTrue(sw.toString().contains("test subheading"));
        assertTrue(sw.toString().contains("<title>test page</title>"));
        assertTrue(sw.toString().contains("<meta name='page_id' content='1'/>"));
    }

    @Test
    public void activeMenuTests() {
        PageInfo pageinfo;
        StringWriter sw;

        pageinfo = PageInfo.getBuilder(1, "test", "test page")
                .heading("test heading")
                .subheading("test subheading")
                .inDevelopmentMode(true)
                .activeMenu(webUI.getActiveMenu("users"))
                .build();
        sw = populatePage("navbar.html", pageinfo);
        assertTrue(sw.toString().contains("li class=\"active\"><a href=\"/users\">Users</a></li>"));
        assertTrue(sw.toString().contains("li><a href=\"/posts\">Posts</a></li>"));

        pageinfo = PageInfo.getBuilder(1, "test", "test page")
                .heading("test heading")
                .subheading("test subheading")
                .inDevelopmentMode(true)
                .activeMenu(webUI.getActiveMenu("posts"))
                .build();
        sw = populatePage("navbar.html", pageinfo);
        assertTrue(sw.toString().contains("li class=\"active\"><a href=\"/posts\">Posts</a></li>"));
        assertTrue(sw.toString().contains("li><a href=\"/users\">Users</a></li>"));
    }

    private StringWriter populatePage(String page, PageInfo pageInfo) {
        File root = getRoot(page);
        MustacheFactory c = new DefaultMustacheFactory(root);
        Mustache m = c.compile(page);
        StringWriter sw = new StringWriter();
        Map<String, Object> scope = new HashMap<>();
        scope.put("pageinfo", pageInfo);
        return (StringWriter) m.execute(sw, scope);
    }

    @Test
    public void factoryTest() throws IOException {
        File root = getRoot("commentinline.html");
        MustacheFactory c = new DefaultMustacheFactory(root);
        Mustache m = c.compile("commentinline.html");
        StringWriter sw = new StringWriter();
        Map<String, String> scope = new HashMap<>();
        scope.put("title", "A Comedy of Errors");
        m.execute(sw, scope);
        assertEquals(getContents(root, "commentinline.txt"), sw.toString());
    }

    @Test
    public void classpathTest() throws Exception {
        ClasspathResolver underTest = new ClasspathResolver("templates");
        Reader reader = underTest.getReader("simple.html");
        assertThat(reader, is(notNullValue()));
    }

    @Test
    public void classpathTestMapping() throws Exception {
        TemplatePathResolver underTest = new TemplatePathResolver();
        Reader reader = underTest.getReader("templates/commentinline.html");
        MustacheFactory c = new DefaultMustacheFactory();
        Mustache m = c.compile(reader, "commentinline.html");
        StringWriter sw = new StringWriter();
        Map<String, Object> scope = new HashMap<>();
        scope.put("title", "A Comedy of Errors");
        m.execute(sw, scope);
        assertEquals(getContents(getRoot(), "commentinline.txt"), sw.toString());
    }

    private File getRoot() {
       return new File("src/test/resources/templates");
    }

    private File getRoot(String fileName) {
        File file = new File("src/test/resources/templates");
        return new File(file, fileName).exists() ? file : new File("src/test/resources/templates");
    }

    @Test
    public void resourceBundleTests() throws Exception {
        String bundle = "messages";

        // region read and prep the template

        TemplatePathResolver bundleTest = new TemplatePathResolver();
        Reader reader = bundleTest.getReader("templates/translatebundle.html");
        MustacheFactory c = new DefaultMustacheFactory();
        Mustache m = c.compile(reader, "translatebundle.html");
        StringWriter sw = new StringWriter();

        // endregion

        Map<String, Object> scope = new HashMap<>();
        scope.put("trans", new TranslateBundleFunction(bundle, Locale.US));
        m.execute(sw, scope);

        // messages.properties contains [jumbotron.title] = "NixMash Microservices"
        assert(sw.toString().contains("NixMash Microservices"));
    }
}
