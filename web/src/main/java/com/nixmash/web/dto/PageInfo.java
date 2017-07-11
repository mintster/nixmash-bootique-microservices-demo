package com.nixmash.web.dto;

import com.nixmash.web.enums.ActiveMenu;

import java.io.Serializable;

/**
 * Created by daveburke on 7/3/17.
 */
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 5250923182891103240L;

    // region properties
    private Integer page_id;
    private String page_key;
    private String page_title;
    private String heading;
    private String subheading;
    private Boolean inDevelopmentMode = false;
    private String menu;
    private ActiveMenu activeMenu;

    // endregion

    // region getter setters

    public Integer getPage_id() {
        return page_id;
    }

    public void setPage_id(Integer page_id) {
        this.page_id = page_id;
    }

    public String getPage_key() {
        return page_key;
    }

    public void setPage_key(String page_key) {
        this.page_key = page_key;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public Boolean getInDevelopmentMode() {
        return inDevelopmentMode;
    }

    public void setInDevelopmentMode(Boolean inDevelopmentMode) {
        this.inDevelopmentMode = inDevelopmentMode;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public ActiveMenu getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(ActiveMenu activeMenu) {
        this.activeMenu = activeMenu;
    }

    // endregion

    // region Builder

    public static Builder getBuilder(Integer page_id, String page_key, String page_title) {
        return new Builder(page_id, page_key, page_title);
    }

    public static class Builder {

        private PageInfo built;

        public Builder(Integer page_id, String page_key, String page_title) {
            built = new PageInfo();
            built.setPage_id(page_id);
            built.setPage_key(page_key);
            built.setPage_title(page_title);
        }

        public Builder heading(String heading) {
            built.setHeading(heading);
            return this;
        }

        public Builder subheading(String subheading) {
            built.setSubheading(subheading);
            return this;
        }

        public Builder inDevelopmentMode(Boolean inDevelopmentMode) {
            built.setInDevelopmentMode(inDevelopmentMode);
            return this;
        }

        public Builder activeMenu(ActiveMenu activeMenu) {
            built.setActiveMenu(activeMenu);
            return this;
        }


        public PageInfo build() {
            return built;
        }
    }

    // endregion

    // region toString()

    @Override
    public String toString() {
        return "PageInfo{" +
                "page_id=" + page_id +
                ", page_key='" + page_key + '\'' +
                ", page_title='" + page_title + '\'' +
                ", heading='" + heading + '\'' +
                ", subheading='" + subheading + '\'' +
                ", inDevelopmentMode=" + inDevelopmentMode +
                ", menu='" + menu + '\'' +
                ", activeMenu=" + activeMenu +
                '}';
    }


    // endregion

}
