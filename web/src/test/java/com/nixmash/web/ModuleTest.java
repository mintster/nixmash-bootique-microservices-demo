package com.nixmash.web;

import com.nixmash.web.controller.GeneralController;
import io.bootique.BQRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.util.ThreadContext;
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
        BQRuntime runtime = testFactory.app("--server", "--config=classpath:test.yml")
                .autoLoadModules()
                .module(binder -> binder.bind(GeneralController.class))
                .createRuntime();

        DefaultSecurityManager sm = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(sm);
        ThreadContext.bind(sm);

        GeneralController controller = runtime.getInstance(GeneralController.class);
        Assert.assertNotNull(controller.home());
    }

}

