package com.nixmash.jangles.service;

import com.google.inject.ImplementedBy;
import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.dto.BearerTokenKey;
import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.enums.JanglesAppId;
import org.apache.shiro.subject.Subject;

import java.util.List;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    List<User> getUsers();

    User createUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);

    User getUserByUserKey(String userKey);

    CurrentUser getCurrentUser(Subject subject);

    BearerAuthenticationToken createBearerToken(CurrentUser currentUser, JanglesAppId appId);

    BearerAuthenticationToken createAnonymousToken();

    Boolean isAnonymousToken(BearerAuthenticationToken token);

    BearerTokenKey decodeBearerToken(BearerAuthenticationToken token);

    Boolean isValidApiKey(BearerTokenKey bearerTokenKey);
}
