package com.nixmash.postservice.utils;

import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.jetty.JettyModule;
import io.bootique.jetty.command.ServerCommand;
import io.bootique.test.junit.BQDaemonTestFactory;
import org.eclipse.jetty.server.Server;

import java.util.function.Function;

import static com.nixmash.postservice.utils.TestUtils.YAML_CONFIG;

public class JettyApp extends BQDaemonTestFactory {

    public void stop() {
        after();
    }

    public void start(String... args) {
        start(b -> {
        }, args);
    }

    @SuppressWarnings("unchecked")
    public BQRuntime start(Module config, String... args) {

        Function<BQRuntime, Boolean> startupCheck = r -> r.getInstance(Server.class).isStarted();

        return app(args)
                .modules(JettyModule.class)
                .module(config)
                .args(YAML_CONFIG)
                .module(binder -> BQCoreModule.extend(binder).setDefaultCommand(ServerCommand.class))
                .startupCheck(startupCheck)
                .start();
    }
}