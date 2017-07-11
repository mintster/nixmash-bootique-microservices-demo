package com.nixmash.web.enums;

/**
 * Created by daveburke on 7/5/17.
 */
public class ActiveMenu {

    private Boolean usersMenu = false;
    private Boolean postsMenu = false;

    public ActiveMenu() {
    }

    public Boolean getUsersMenu() {
        return usersMenu;
    }

    public void setUsersMenu(Boolean usersMenu) {
        this.usersMenu = usersMenu;
    }

    public Boolean getPostsMenu() {
        return postsMenu;
    }

    public void setPostsMenu(Boolean postsMenu) {
        this.postsMenu = postsMenu;
    }
}
