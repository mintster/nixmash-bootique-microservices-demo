package com.nixmash.web.views;

import io.bootique.mvc.AbstractView;

public class BaseView extends AbstractView {

    private String title;

    public BaseView(String template, String title) {
        super(template);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}