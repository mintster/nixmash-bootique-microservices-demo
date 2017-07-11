package com.nixmash.jangles.core;

import com.nixmash.jangles.model.JanglesUser;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by daveburke on 6/12/17.
 */
public class TestUtils {

    private static final String PASSWORD = "password";

    public static JanglesUser createJanglesUser(String username) {
       return new JanglesUser(
                username,
                PASSWORD,
                String.format("%s Smith", StringUtils.capitalize(username)));
    }
}
