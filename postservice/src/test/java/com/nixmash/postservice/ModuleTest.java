package com.nixmash.postservice;

import com.nixmash.postservice.core.PostServiceConfig;
import io.bootique.BQRuntime;
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
        BQRuntime runtime = testFactory.app("--server", "--config=classpath:test.yml")
                .autoLoadModules()
                .module(binder -> binder.bind(PostServiceConfig.class))
                .createRuntime();

        PostServiceConfig config = runtime.getInstance(PostServiceConfig.class);
        assertTrue(config.applicationId.contains("test"));

    }

}
