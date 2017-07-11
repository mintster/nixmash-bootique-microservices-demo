package com.nixmash.web.views;


import com.nixmash.jangles.model.JanglesUser;

import java.util.List;

public class UsersView extends BaseView {

    private final List<JanglesUser> users;

    public UsersView(List<JanglesUser> users) {
        super("users.mustache", "Bootique Mvc Mustache Demo - Users");
        this.users = users;
    }

    public List<JanglesUser> getUsers() {
        return users;
    }
}