package com.nixmash.jangles;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.core.JanglesContext;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.core.JanglesLocalizer;
import com.nixmash.jangles.guice.GuiceJUnit4Runner;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by daveburke on 6/6/17.
 */
@RunWith(GuiceJUnit4Runner.class)
public class CoreTest {

    private static final Logger logger = LoggerFactory.getLogger(CoreTest.class);

    @Inject
    private JanglesGlobals janglesGlobals;

    @Inject
    private JanglesContext janglesContext;

    @Test
    public void loggingTest() {
        assertNotNull(logger.isInfoEnabled());
    }

    @Test
    public void globalsTests() {
        String cloudApplicationId = janglesGlobals.cloudApplicationId;
        assertNotNull(cloudApplicationId);
    }

    @Test
    public void contextTests() {
        assertNotNull(janglesContext.config().applicationId);
        assertNotNull(janglesContext.messages().get("greetings"));
    }

    @Test
    public void localizationTests() {

        JanglesConfiguration janglesConfiguration = new JanglesConfiguration();
        janglesConfiguration.currentLocale = "fr_FR";
        JanglesLocalizer janglesLocalizer = new JanglesLocalizer(janglesConfiguration);
        JanglesContext mockContext = new JanglesContext(janglesConfiguration, janglesLocalizer);

        String greetings = mockContext.messages().get("greetings");
        assertTrue(greetings.contains("in french"));
    }

    @Test
    public void localizationParsingTests() {
        JanglesConfiguration janglesConfiguration = new JanglesConfiguration();
        JanglesLocalizer janglesLocalizer = new JanglesLocalizer(janglesConfiguration);
        assertEquals(janglesLocalizer.get("test.title"), "My Test Title");
        assertEquals(janglesLocalizer.get("test.title.with.two.params", "One", "Two"), "My One and Two Test Title");
    }

    @Test
    public void emptyCurrentLocaleTests() {

        JanglesConfiguration janglesConfiguration = new JanglesConfiguration();
        janglesConfiguration.currentLocale = StringUtils.EMPTY;
        JanglesLocalizer janglesLocalizer = new JanglesLocalizer(janglesConfiguration);
        JanglesContext mockContext = new JanglesContext(janglesConfiguration, janglesLocalizer);

        String greetings = mockContext.messages().get("greetings");
        assertNotNull(greetings);
    }

}
