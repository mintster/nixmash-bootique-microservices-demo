package com.nixmash.userservice;

import com.nixmash.userservice.core.UserServiceConfig;
import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

/**
 * Created by daveburke on 6/13/17.
 */
@RunWith(JUnit4.class)
public class ModuleTest {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testRuntime() {
        BQTestRuntime runtime = testFactory.app("--server", "--config=classpath:bootique-tests.yml")
                .autoLoadModules()
                .module(binder -> binder.bind(UserServiceConfig.class))
                .createRuntime();

        UserServiceConfig config = runtime.getRuntime().getInstance(UserServiceConfig.class);
        assertTrue(config.applicationId.contains("test"));

    }

}
