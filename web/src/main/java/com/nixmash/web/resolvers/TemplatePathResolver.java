package com.nixmash.web.resolvers;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.resolver.ClasspathResolver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by daveburke on 6/29/17.
 */
public class TemplatePathResolver implements MustacheResolver {

    private final String resourceRoot;

    public TemplatePathResolver() {
        this.resourceRoot = "templates/";
    }

    public Reader getReader(String resourceName) {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        String name = this.resourceRoot + resourceName;
        InputStream is = ccl.getResourceAsStream(name);
        if(is == null) {
            is = ClasspathResolver.class.getClassLoader().getResourceAsStream(name);
        }

        return is != null?new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)):null;
    }

    public String getResourceRoot() {
        return this.resourceRoot;
    }

    public String populateTemplate(String template, Map<String, Object> model) {
        TemplatePathResolver resolver = new TemplatePathResolver();
        Reader reader = resolver.getReader(template);
        MustacheFactory c = new DefaultMustacheFactory();
        Mustache m = c.compile(reader, template);
        StringWriter sw = new StringWriter();
        m.execute(sw, model);
        return sw.toString();
    }
}
