package com.nixmash.jangles.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CurrentUser implements Serializable{

    private static final long serialVersionUID = 6302130928243440723L;

    private Long userId;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    private Boolean isAdministrator = false;
    private String displayName;
    private String userKey;

    public CurrentUser() {
    }

    public CurrentUser(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userKey = user.getUserKey();
        this.displayName = user.getFirstName() + " " + user.getLastName();
    }

    // region getter setter

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Boolean getAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(Boolean administrator) {
        isAdministrator = administrator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    // endregion


    @Override
    public String toString() {
        return "CurrentUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userKey='" + userKey + '\'' +
                ", displayName='" + displayName + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}
