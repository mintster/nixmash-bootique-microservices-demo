package com.nixmash.userservice;

import com.google.inject.Inject;
import com.nixmash.userservice.core.UserServiceGlobals;
import com.nixmash.userservice.guice.GuiceJUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

/**
 * Created by daveburke on 6/6/17.
 */
@RunWith(GuiceJUnit4Runner.class)
public class CoreTest {

    @Inject
    private UserServiceGlobals userServiceGlobals;

    private static final Logger logger = LoggerFactory.getLogger(CoreTest.class);

    @Test
    public void loggingTest() {
        assertNotNull(logger.isInfoEnabled());
    }

    @Test
    public void globalsTests() {
        String cloudApplicationId = userServiceGlobals.cloudApplicationId;
        assertNotNull(cloudApplicationId);
    }

}
