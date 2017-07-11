package com.nixmash.jangles.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.jangles.db.IConnection;
import com.nixmash.jangles.db.MySqlConnection;

/**
 * Created by daveburke on 6/17/17.
 */
public class JanglesModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(IConnection.class).to(MySqlConnection.class);
    }
}
