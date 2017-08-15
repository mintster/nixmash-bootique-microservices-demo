package com.nixmash.web.auth;

import com.google.inject.Inject;
import com.nixmash.jangles.db.IConnection;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import com.nixmash.web.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NixmashRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(NixmashRealm.class);

    private UserService userService;
    private IConnection iConnection;

    @Inject
    public NixmashRealm(UserService userService) {
        setName("nixmashRealm");
        this.userService = userService;
        this.iConnection = iConnection;

        HashedCredentialsMatcher authenticator =
                new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
        this.setCredentialsMatcher(authenticator);
    }

    @Override
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
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        String username = userPassToken.getUsername();

        AuthenticationInfo info = null;
        try {

            final User user = userService.getUser(username);
            if (user == null) {
                System.out.println("No account found for user [" + username + "]");
                return null;
            }
            info = new SimpleAuthenticationInfo(username, user.getPassword(),  getName());
        } catch (AuthenticationException e) {
            final String message = "There was an error while authenticating user [" + username + "]";
            if (logger.isErrorEnabled()) {
                logger.error(message, e);
            }
        }
        return info;
    }
}
