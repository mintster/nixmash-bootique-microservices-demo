package com.nixmash.jangles.service;

import com.google.inject.Inject;
import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.dto.BearerTokenKey;
import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.enums.JanglesAppId;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final String CURRENT_USER = "CurrentUser";
    private static final String NA = "na";


    private UsersDb usersDb;
    private JanglesGlobals janglesGlobals;

    @Inject
    public UserServiceImpl(UsersDb usersDb, JanglesGlobals janglesGlobals) {
        this.usersDb = usersDb;
        this.janglesGlobals = janglesGlobals;
    }

    // region Users and Roles

    @Override
    public User getUser(String username) {
        User user = null;
        try {
            user = usersDb.getUser(username);
        } catch (SQLException e) {}
        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = usersDb.getUsers();
        } catch (SQLException e) {}
        return users;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        logger.info("User with email:" + user.getEmail() + " hashedPassword:" +user.getPassword());
        try {
            user = usersDb.addUser(user);
        } catch (SQLException e) {}
        return user;
    }

    @Override
    public List<Role> getRoles(Long userId) {
        List<Role> roles = null;
        try {
            roles =  usersDb.getRoles(userId);
        }
        catch (SQLException e) {}
        return roles;
    }

    @Override
    public User getUserByUserKey(String userKey) {
        try {
            return usersDb.getUserByUserKey(userKey);
        } catch (SQLException e) {
            return new User();
        }
    }

    // endregion

    // region CurrentUser

    @Override
    public CurrentUser getCurrentUser(Subject subject) {
        User user = this.getUser(subject.getPrincipals().toString());
        CurrentUser currentUser = new CurrentUser(user);
        List<Role> roles = this.getRoles(user.getUserId());

        for (Role role : roles) {
            if (role.getRoleName().equals("admin")) {
                currentUser.setAdministrator(true);
            }
            currentUser.getRoles().add(role.getRoleName());
            currentUser.getPermissions().add(role.getPermission());
        }
        return currentUser;
    }

    // endregion


    // region Authentication Tokens

    @Override
    public BearerAuthenticationToken createBearerToken(CurrentUser currentUser, JanglesAppId appId) {
        String userKey = currentUser.getUserKey();
        String serviceApiKey = appId != null ? getServiceApiKey(appId) : String.valueOf(JanglesAppId.NA);
        String encoded = Base64.encodeToString(MessageFormat.format("{0}:{1}:{2}", userKey, serviceApiKey, appId).getBytes());
        return new BearerAuthenticationToken(encoded);
    }

    @Override
    public BearerAuthenticationToken createAnonymousToken() {
        String encoded = Base64.encodeToString(MessageFormat.format("{0}:{1}:{2}", NA, NA, JanglesAppId.NA).getBytes());
        return new BearerAuthenticationToken(encoded);
    }

    @Override
    public Boolean isAnonymousToken(BearerAuthenticationToken token) {
        BearerTokenKey bearerTokenKey = decodeBearerToken(token);
        return bearerTokenKey.getUserkey().equals(NA);
    }

    @Override
    public BearerTokenKey decodeBearerToken(BearerAuthenticationToken token) {
        String[] keys = new String(Base64.decode(token.getToken())).split(":");
        BearerTokenKey bearerTokenKey = new BearerTokenKey(NA,NA, JanglesAppId.NA);
        try {
            bearerTokenKey = new BearerTokenKey(keys[0],
                    keys[1],
                    JanglesAppId.valueOf(keys[2]));
        } catch (Exception e) {
            logger.error("BearerTokenKey Decode Exception: " + e.getMessage());
        }
        return bearerTokenKey;
    }

    @Override
    public Boolean isValidApiKey(BearerTokenKey bearerTokenKey) {
        String apiKey = bearerTokenKey.getApikey();
        JanglesAppId appId = bearerTokenKey.getAppId();
        return apiKey.equals(getServiceApiKey(appId));
    }

    private String getServiceApiKey(JanglesAppId appId) {
        String apiKey = null;
        switch (appId) {
            case USER_SERVICE:
                apiKey = janglesGlobals.userServiceApiKey;
                break;
            case WEB_CLIENT:
                apiKey = janglesGlobals.webApiKey;
                break;
            case NA:
                break;
        }
        return apiKey;
    }

    // endregion
}
