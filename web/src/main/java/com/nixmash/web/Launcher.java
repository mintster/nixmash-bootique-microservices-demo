package com.nixmash.web;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.web.controller.GeneralController;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daveburke on 6/26/17.
 */
public class Launcher implements Module {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    //@formatter:off

    public static void main(String[] args) {

        Bootique
                .app(args)
                .autoLoadModules()
                .module(Launcher.class)
//                .args("-H")
                .args("--server", "--config=classpath:bootique.yml")
                .run();
    }

    //@formatter:on

    @Override
    public void configure(Binder binder) {

        JerseyModule.extend(binder)
                .addResource(GeneralController.class);

        JettyModule.extend(binder).addStaticServlet("s1", "/css/*", "/img/*", "/js/*", "/fonts/*");

    }


}
