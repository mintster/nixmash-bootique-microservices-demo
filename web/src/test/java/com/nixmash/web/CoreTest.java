package com.nixmash.web;

import com.google.inject.Inject;
import com.nixmash.web.core.WebContext;
import com.nixmash.web.core.WebGlobals;
import com.nixmash.web.core.WebUI;
import com.nixmash.web.dto.PageInfo;
import com.nixmash.web.guice.GuiceJUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by daveburke on 6/6/17.
 */
@RunWith(GuiceJUnit4Runner.class)
public class CoreTest {

    @Inject
    private WebGlobals webGlobals;

    @Inject
    private WebContext webContext;

    @Inject
    private WebUI webUI;

    private static final Logger logger = LoggerFactory.getLogger(CoreTest.class);

    @Test
    public void loggingTest() {
        assertNotNull(logger.isInfoEnabled());
    }

    @Test
    public void globalsTests() {
        String cloudApplicationId = webGlobals.cloudApplicationId;
        assertNotNull(cloudApplicationId);
    }

    @Test
    public void configPropertyTests() {
        String applicationId = webContext.config().applicationId;
        assertTrue(applicationId.contains("testing"));
    }

    @Test
    public void contextTests() {
        assertNotNull(webContext.config().applicationId);
    }

    @Test
    public void pageInfoTests() throws IOException {
        PageInfo pageInfo = webUI.getPageInfo("home");
    }

    @Test
    public void messageTests() throws Exception {
        assertEquals(webContext.messages().get("test.title"), "My Web Test Title");
        assertEquals(webContext.messages().get("test.title.with.two.params", "One", "Two"), "My Web One and Two Test Title");
    }
}
