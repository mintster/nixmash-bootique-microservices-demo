package com.nixmash.jangles.ui;

import com.google.inject.Inject;
import com.nixmash.jangles.db.IConnection;
import com.nixmash.jangles.service.JanglesApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JanglesUI {

    private static final Logger logger = LoggerFactory.getLogger(JanglesUI.class);
    private final JanglesApiService janglesApiService;
    private final IConnection iConnection;

    @Inject
    public JanglesUI(IConnection iConnection, JanglesApiService janglesApiService) {
        this.iConnection = iConnection;
        this.janglesApiService = janglesApiService;
    }

    public void init() {
        logger.info(janglesApiService.showConfiguration());
        logger.info(janglesApiService.sayHello());
//        showMessages();
    }

    // region users


    // region properties

    public void showMessages() {
        String language;
        String country;

        Locale currentLocale;
        ResourceBundle messages;

        currentLocale = new Locale("en", "US");

        messages = ResourceBundle.getBundle("messages", currentLocale);
        System.out.println(messages.getString("greetings"));
        System.out.println(messages.getString("inquiry"));
        System.out.println(messages.getString("farewell"));
    }

    public void showAllProperties() {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }

        Properties systemProperties = System.getProperties();
        Enumeration<?> enuProp = systemProperties.propertyNames();
        while (enuProp.hasMoreElements()) {
            String propertyName = (String) enuProp.nextElement();
            String propertyValue = systemProperties.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
        }
    }

    // endregion

}
