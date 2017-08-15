package com.nixmash.jangles.db;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesConnections;

/**
 * Created by daveburke on 6/17/17.
 */
public class MySqlConnection implements IConnection {

    @Inject
    JanglesConnections janglesConnections;

    @Override
    public JanglesConnection get() {
        return janglesConnections.getMySqlConnection();
    }
}
