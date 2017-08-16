package com.nixmash.web;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.db.cn.MySqlConnection;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.web.auth.NixmashRealm;
import com.nixmash.web.auth.NixmashRoleFilter;
import com.nixmash.web.controller.GeneralController;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;
import io.bootique.Bootique;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.shiro.ShiroModule;
import io.bootique.shiro.web.ShiroWebModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daveburke on 6/26/17.
 */
public class Launcher implements Module {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        Bootique
                .app(args)
                .autoLoadModules()
                .module(Launcher.class)
                .args("--server", "--config=classpath:bootique.yml")
                .exec();
    }


    @Override
    public void configure(Binder binder) {

        binder.bind(UserService.class).to(UserServiceImpl.class);
        binder.bind(UsersDb.class).to(UsersDbImpl.class);
        binder.bind(IConnection.class).to(MySqlConnection.class);

        Package pkg = GeneralController.class.getPackage();
        JerseyModule.extend(binder).addPackage(pkg);

        JettyModule.extend(binder)
                .addStaticServlet("s1", "/css/*", "/img/*", "/js/*", "/fonts/*");

        ShiroModule.extend(binder).addRealm(NixmashRealm.class);
        ShiroWebModule.extend(binder).setFilter("roles", NixmashRoleFilter.class);

    }


}
