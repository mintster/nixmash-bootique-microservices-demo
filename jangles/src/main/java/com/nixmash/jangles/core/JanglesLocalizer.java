package com.nixmash.jangles.core;

import com.google.inject.Inject;
import org.apache.commons.lang3.LocaleUtils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by daveburke on 7/3/17.
 */
public class JanglesLocalizer {

    private final JanglesConfiguration janglesConfiguration;

    @Inject
    public JanglesLocalizer(JanglesConfiguration janglesConfiguration) {
        this.janglesConfiguration = janglesConfiguration;
    }

    private ResourceBundle getResourceBundle() {
        Locale currentLocale;
        currentLocale = LocaleUtils.toLocale(janglesConfiguration.currentLocale);
        return ResourceBundle.getBundle("messages", currentLocale);
    }

    public String get(String code, Object... params) {
        String message = this.getResourceBundle().getString(code);
        MessageFormat mf = new MessageFormat(message);
       return mf.format(params);
    }

}
