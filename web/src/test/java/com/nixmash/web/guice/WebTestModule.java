package com.nixmash.web.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.db.IConnection;

/**
 * Created by daveburke on 7/1/17.
 */
public class WebTestModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(IConnection.class).to(TestConnection.class);
    }

}
