

package com.nixmash.jangles.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable{

	private static final long serialVersionUID = -8144868102971802127L;

	public User() {}

	// region properties

	public Long userId;
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
	public Integer version;
	public Integer loginAttempts;
	public Timestamp lastloginDatetime;
	public Timestamp createdDatetime;
	public Timestamp approvedDatetime;
	public Timestamp invitedDatetime;
	public Timestamp acceptedDatetime;
	public Long invitedById;
	public String ip;


	// endregion

	// region constructor

	public User(String username, String email, String firstName, String lastName, String password) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}

	// endregion

	// region getters/setters

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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Integer loginAttempts) {
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

	public Long getInvitedById() {
		return invitedById;
	}

	public void setInvitedById(Long invitedById) {
		this.invitedById = invitedById;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



	// endregion

	// region tostring

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", enabled=" + enabled +
				", accountExpired=" + accountExpired +
				", accountLocked=" + accountLocked +
				", credentialsExpired=" + credentialsExpired +
				", hasAvatar=" + hasAvatar +
				", userKey='" + userKey + '\'' +
				", providerId='" + providerId + '\'' +
				", password='" + password + '\'' +
				", version=" + version +
				", loginAttempts=" + loginAttempts +
				", lastloginDatetime=" + lastloginDatetime +
				", createdDatetime=" + createdDatetime +
				", approvedDatetime=" + approvedDatetime +
				", invitedDatetime=" + invitedDatetime +
				", acceptedDatetime=" + acceptedDatetime +
				", invitedById=" + invitedById +
				", ip='" + ip + '\'' +
				'}';
	}


	// endregion
}



