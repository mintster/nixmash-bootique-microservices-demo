package com.nixmash.web.core;

import com.google.inject.Inject;

import java.io.Serializable;

/**
 * Created by daveburke on 7/3/17.
 */
public class WebContext implements Serializable{

    private final WebConfig webConfig;
    private final WebLocalizer webLocalizer;
    private final WebGlobals webGlobals;

    @Inject
    public WebContext(WebConfig webConfig, WebLocalizer webLocalizer, WebGlobals webGlobals) {
        this.webConfig = webConfig;
        this.webLocalizer = webLocalizer;
        this.webGlobals = webGlobals;
    }


    public WebConfig config() {
        return this.webConfig;
    }

    public WebLocalizer messages() {
        return webLocalizer;
    }

    public WebGlobals globals() {
        return this.webGlobals;
    }

}
