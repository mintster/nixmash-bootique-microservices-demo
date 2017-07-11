package com.nixmash.jangles.service;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.core.JanglesGlobals;

/**
 * Created by daveburke on 6/19/17.
 */
public class JanglesApiServiceImpl implements JanglesApiService {


    JanglesGlobals janglesGlobals;
    JanglesConfiguration janglesConfiguration;

    @Inject
    public JanglesApiServiceImpl(JanglesGlobals janglesGlobals, JanglesConfiguration janglesConfiguration) {
        this.janglesGlobals = janglesGlobals;
        this.janglesConfiguration = janglesConfiguration;
    }

    @Override
    public String showConfiguration() {
        {
            String _configuration = janglesConfiguration.applicationId + " : "
                    + "GLOBAL APIKEY: " + janglesGlobals.apiKey + " : "
                    + "GLOBAL PROPFILE: " + janglesConfiguration.globalPropertiesFile + " : ";
            return _configuration;
        }
    }

    @Override
    public String sayHello() {
        return "Hello from Jangles API Service Implementation";
    }
}
