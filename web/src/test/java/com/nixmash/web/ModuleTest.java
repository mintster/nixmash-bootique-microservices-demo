package com.nixmash.web;

import com.nixmash.web.controller.GeneralController;
import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * Created by daveburke on 6/26/17.
 */
@RunWith(JUnit4.class)
public class ModuleTest {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void loadModuleTest() throws IOException {
        BQTestRuntime runtime = testFactory.app("--server", "--config=classpath:bootique-tests.yml")
                .autoLoadModules()
                .module(binder -> binder.bind(GeneralController.class))
                .createRuntime();

        GeneralController controller = runtime.getRuntime().getInstance(GeneralController.class);
        Assert.assertNotNull(controller.home());
    }

}
