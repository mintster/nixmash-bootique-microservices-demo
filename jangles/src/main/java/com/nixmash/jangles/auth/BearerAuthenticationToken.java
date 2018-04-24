package com.nixmash.jangles.auth;

import org.apache.shiro.authc.AuthenticationToken;

public class BearerAuthenticationToken implements AuthenticationToken {

    String token;
    private String username;
    private char[] password;

    public BearerAuthenticationToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return this.getUsername();
    }

    @Override
    public Object getCredentials() {
        return this.getPassword();
    }

    public String getToken() {
        return this.token;
    }
}