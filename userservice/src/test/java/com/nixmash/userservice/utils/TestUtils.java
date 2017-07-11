package com.nixmash.userservice.utils;

import com.nixmash.jangles.model.JanglesUser;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by daveburke on 6/12/17.
 */
public class TestUtils {

    public static final String TEST_URL = "http://127.0.1.1:8001";
    public static final String YAML_CONFIG = "--config=classpath:bootique-tests.yml";
    private static final String PASSWORD = "password";

    public static JanglesUser createJanglesUser(String username) {
       return new JanglesUser(
                username,
                PASSWORD,
                String.format("%s Smith", StringUtils.capitalize(username)));
    }
}
