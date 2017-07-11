package com.nixmash.web.views;

// intentionally keeping the view class in a different package
// from API and IT to test per-package template laoding
public class HomeView extends BaseView {

private String siteTitle;
private String siteSubtitle;

    public HomeView() {
        super("home.mustache", "Bootique Mvc Mustache Demo - Home Page");
        this.siteTitle = "Bootique Mvc Mustache";
        this.siteSubtitle = "Simple Example Application";
    }

    public String getSiteTitle() {
        return siteTitle;
    }

    public String getSiteSubtitle() {
        return siteSubtitle;
    }
}