package com.nixmash.jangles.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.service.JanglesApiService;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;

/**
 * Created by daveburke on 6/18/17.
 */
public class JanglesTestModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(IConnection.class).to(TestConnection.class);
        binder.bind(UsersDb.class).to(UsersDbImpl.class);
        binder.bind(UserService.class).to(UserServiceImpl.class);
        binder.bind(JanglesApiService.class).to(TestApiServiceImpl.class);
    }

}
