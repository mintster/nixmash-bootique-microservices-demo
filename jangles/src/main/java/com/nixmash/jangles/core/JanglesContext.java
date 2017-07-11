package com.nixmash.jangles.core;

import com.google.inject.Inject;

import java.io.Serializable;

/**
 * Created by daveburke on 7/3/17.
 */
public class JanglesContext implements Serializable{

    private final JanglesConfiguration janglesConfiguration;
    private final JanglesLocalizer janglesLocalizer;

    @Inject
    public JanglesContext(JanglesConfiguration janglesConfiguration, JanglesLocalizer janglesLocalizer) {
        this.janglesConfiguration = janglesConfiguration;
        this.janglesLocalizer = janglesLocalizer;
    }

    public JanglesConfiguration config() {
        return this.janglesConfiguration;
    }

    public JanglesLocalizer messages() {
        return janglesLocalizer;
    }

}
