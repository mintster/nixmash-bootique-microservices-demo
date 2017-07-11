package com.nixmash.jangles.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class JanglesConnection implements java.io.Serializable {

    private static final long serialVersionUID = 1268527199886230105L;

    public JanglesConnection() {};

    // region Properties

    private String name;
    private String environment;
    private String username;
    private String password;
    private String urlbase;
    private String database;

    // endregion

    // region Getter Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlbase() {
        return urlbase;
    }

    public void setUrlbase(String urlbase) {
        this.urlbase = urlbase;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUrl() {
        return this.urlbase + this.database;
    }

    // endregion

    @Override
    public String toString() {
        return "JanglesConnection{" +
                "name='" + name + '\'' +
                ", environment='" + environment + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", urlbase='" + urlbase + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
