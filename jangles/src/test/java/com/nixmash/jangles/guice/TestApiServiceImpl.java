package com.nixmash.jangles.guice;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.service.JanglesApiService;

/**
 * Created by daveburke on 6/19/17.
 */
public class TestApiServiceImpl implements JanglesApiService{

    JanglesGlobals janglesGlobals;
    JanglesConfiguration janglesConfiguration;

    @Inject
    public TestApiServiceImpl(JanglesGlobals janglesGlobals, JanglesConfiguration janglesConfiguration) {
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
        return "Hello from TestAPIServiceImpl";
    }
}
