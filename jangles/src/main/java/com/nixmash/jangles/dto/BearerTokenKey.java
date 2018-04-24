package com.nixmash.jangles.dto;

import com.nixmash.jangles.enums.JanglesAppId;

import java.io.Serializable;

public class BearerTokenKey implements Serializable {
    private static final long serialVersionUID = -3278908642648475686L;

    private String userkey;
    private String apikey;
    private JanglesAppId appId;

    public BearerTokenKey(String userkey, String apikey, JanglesAppId appId) {
        this.userkey = userkey;
        this.apikey = apikey;
        this.appId = appId;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public JanglesAppId getAppId() {
        return appId;
    }

    public void setAppId(JanglesAppId appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "BearerTokenKey{" +
                "userkey='" + userkey + '\'' +
                ", apikey='" + apikey + '\'' +
                ", appId=" + appId +
                '}';
    }
}
