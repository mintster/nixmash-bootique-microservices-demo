


package com.nixmash.jangles.dto;

import java.sql.Timestamp;

public class TUser {

    public TUser() {
    }

    // region properties

    public long userId;
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public Boolean enabled;
    public Boolean accountExpired;
    public Boolean accountLocked;
    public Boolean credentialsExpired;
    public Boolean hasAvatar;
    public String userKey;
    public String providerId;
    public String password;
    public int version;
    public int loginAttempts;
    public Timestamp lastloginDatetime;
    public Timestamp createdDatetime;
    public Timestamp approvedDatetime;
    public Timestamp invitedDatetime;
    public Timestamp acceptedDatetime;
    public long invitedById;
    public String ip;


    // endregion

    // region getters/setters

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getHasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(Boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Timestamp getLastloginDatetime() {
        return lastloginDatetime;
    }

    public void setLastloginDatetime(Timestamp lastloginDatetime) {
        this.lastloginDatetime = lastloginDatetime;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Timestamp getApprovedDatetime() {
        return approvedDatetime;
    }

    public void setApprovedDatetime(Timestamp approvedDatetime) {
        this.approvedDatetime = approvedDatetime;
    }

    public Timestamp getInvitedDatetime() {
        return invitedDatetime;
    }

    public void setInvitedDatetime(Timestamp invitedDatetime) {
        this.invitedDatetime = invitedDatetime;
    }

    public Timestamp getAcceptedDatetime() {
        return acceptedDatetime;
    }

    public void setAcceptedDatetime(Timestamp acceptedDatetime) {
        this.acceptedDatetime = acceptedDatetime;
    }

    public long getInvitedById() {
        return invitedById;
    }

    public void setInvitedById(long invitedById) {
        this.invitedById = invitedById;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    // endregion

}



