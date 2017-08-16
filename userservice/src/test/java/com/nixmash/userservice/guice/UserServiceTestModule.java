package com.nixmash.userservice.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.db.cn.IConnection;

/**
 * Created by daveburke on 6/18/17.
 */
public class UserServiceTestModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(IConnection.class).to(TestConnection.class);
    }

}
