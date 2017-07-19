package com.nixmash.web.core;

import com.google.inject.Inject;
import com.nixmash.web.resolvers.TemplatePathResolver;

import java.io.Serializable;

/**
 * Created by daveburke on 7/3/17.
 */
public class WebContext implements Serializable{

    private final WebConfig webConfig;
    private final WebLocalizer webLocalizer;
    private final WebGlobals webGlobals;
    private final TemplatePathResolver templatePathResolver;

    @Inject
    public WebContext(WebConfig webConfig, WebLocalizer webLocalizer, WebGlobals webGlobals, TemplatePathResolver templatePathResolver) {
        this.webConfig = webConfig;
        this.webLocalizer = webLocalizer;
        this.webGlobals = webGlobals;
        this.templatePathResolver = templatePathResolver;
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

    public TemplatePathResolver templatePathResolver() {
        return this.templatePathResolver;
    }
}
