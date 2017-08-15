package com.nixmash.jangles.ui;

import com.google.inject.Inject;
import com.nixmash.jangles.service.JanglesApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanglesUI {

    private static final Logger logger = LoggerFactory.getLogger(JanglesUI.class);
    private final JanglesApiService janglesApiService;

    @Inject
    public JanglesUI(JanglesApiService janglesApiService) {
        this.janglesApiService = janglesApiService;
    }

    public void init() {
        logger.info(janglesApiService.showConfiguration());
        logger.info(janglesApiService.sayHello());
    }

}
