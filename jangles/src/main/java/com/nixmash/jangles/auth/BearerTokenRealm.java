package com.nixmash.jangles.auth;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.dto.BearerTokenKey;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;

public class BearerTokenRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(BearerTokenRealm.class);

    private UserService userService;
    private JanglesGlobals janglesGlobals;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BearerAuthenticationToken;
    }

    @Inject
    public BearerTokenRealm(UserService userService, JanglesGlobals janglesGlobals) {
        setName("bearerTokenRealm");
        this.userService = userService;
        this.janglesGlobals = janglesGlobals;

    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Long userid = userService.getUser(principals.getPrimaryPrincipal().toString()).getUserId();
        List<Role> roles = userService.getRoles(userid);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role role : roles) {
            info.addStringPermission(role.getPermission());
            info.addRole(role.getRoleName());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {

        BearerAuthenticationToken bearerToken = (BearerAuthenticationToken)token;
        BearerTokenKey bearerTokenKey = userService.decodeBearerToken(bearerToken);

        // TODO: add ApplicationApiKey condition
        if (!userService.isValidApiKey(bearerTokenKey)) {
            String errMsg = MessageFormat.format("Invalid Api Key [{0}] for [{1}]", bearerTokenKey.getApikey(), bearerTokenKey.getAppId().name());
            throw new AuthenticationException(errMsg);
        }

        User user = new User();
        AuthenticationInfo info = null;
        try {
            user = userService.getUserByUserKey(bearerTokenKey.getUserkey());
            if (user.getUsername() == null) {
                logger.error("No account found for userkey [" + bearerTokenKey.getUserkey() + "]");
                return null;
            }
            bearerToken.setUsername(user.getUsername());
            bearerToken.setPassword(user.getPassword().toCharArray());
            info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),  getName());
        } catch (AuthenticationException e) {
            final String message = "There was an error while authenticating user [" + user.getUsername() + "]";
            if (logger.isErrorEnabled()) {
                logger.error(message, e);
            }
        }
        return info;
    }
}
