package com.nixmash.web.core;

import com.google.inject.Inject;
import org.apache.commons.lang3.LocaleUtils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by daveburke on 7/3/17.
 */
public class WebLocalizer {

    private final WebConfig webConfig;

    @Inject
    public WebLocalizer(WebConfig janglesConfiguration) {
        this.webConfig = janglesConfiguration;
    }

    private ResourceBundle getResourceBundle() {
        Locale currentLocale;
        currentLocale = LocaleUtils.toLocale(webConfig.currentLocale);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
        System.out.println(bundle.getBaseBundleName());
        return ResourceBundle.getBundle("messages", currentLocale);
    }

    public String get(String key, Object... params) {
        String message = this.getResourceBundle().getString(key);
        MessageFormat mf = new MessageFormat(message);
        return mf.format(params);
    }

}
