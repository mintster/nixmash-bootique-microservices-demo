package com.nixmash.postservice.shiro;

import com.google.inject.Inject;
import com.nixmash.jangles.auth.BearerAuthenticationToken;
import com.nixmash.jangles.auth.BearerTokenRealm;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.jangles.db.UsersDb;
import com.nixmash.jangles.db.UsersDbImpl;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.dto.BearerTokenKey;
import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.enums.JanglesAppId;
import com.nixmash.jangles.service.UserService;
import com.nixmash.jangles.service.UserServiceImpl;
import com.nixmash.postservice.guice.TestConnection;
import io.bootique.BQRuntime;
import io.bootique.shiro.ShiroModule;
import io.bootique.test.junit.BQTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BearerTokenTest {

    private static final String BOB = "bob";

    @ClassRule
    public static BQTestFactory testFactory = new BQTestFactory();

    private static BQRuntime runtime;

    private static UserService userService;
    private static JanglesGlobals janglesGlobals;
    private static BearerTokenRealm bearerTokenRealm;

    @Inject
    private static BearerAuthenticationToken bearerAuthenticationToken;

    private static Subject subject;

    @BeforeClass
    public static void beforeClass() {
        runtime = testFactory
                .app("--config=classpath:test.yml")
                .module(b -> b.bind(IConnection.class).to(TestConnection.class))
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> b.bind(JanglesGlobals.class))
                .module(b -> ShiroModule.extend(b).addRealm(BearerTokenRealm.class))
                .autoLoadModules()
                .createRuntime();

        userService = runtime.getInstance(UserService.class);
        janglesGlobals = runtime.getInstance(JanglesGlobals.class);

        try {
            configureTestDb("populate.sql");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTokenLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        User bob = userService.getUser(BOB);
        subject.login(userService.createBearerToken(new CurrentUser(bob), JanglesAppId.USER_SERVICE));
        subject.checkRole("user");
        subject.logout();
    }

    @Test
    public void tokenCreationTest() {
        String userKey = "12345";
        String serviceApiKey = janglesGlobals.userServiceApiKey;
        String encoded = Base64.encodeToString(MessageFormat.format("{0}:{1}:{2}", userKey, serviceApiKey, JanglesAppId.USER_SERVICE).getBytes());
        String[] keys = new String(Base64.decode(encoded)).split(":");
        assertEquals(keys[0], userKey);
        assertEquals(keys[1], serviceApiKey);
        assertEquals(keys[2], JanglesAppId.USER_SERVICE.name());
    }

    @Test
    public void tokenCreationFromMethodTest() {
        BearerAuthenticationToken token = createBearerTestToken();
        assertTrue(token.getToken().startsWith("VzRhZ"));
    }

    @Test
    public void decodeBearerTokenTest() {
        BearerAuthenticationToken token = createBearerTestToken();
        BearerTokenKey bearerTokenKey = userService.decodeBearerToken(token);
        User user = userService.getUser(BOB);
        assertEquals(bearerTokenKey.getUserkey(), user.getUserKey());
    }

    private BearerAuthenticationToken createBearerTestToken() {
        User bob = userService.getUser(BOB);
        CurrentUser currentUser = new CurrentUser(bob);
        return userService.createBearerToken(currentUser, JanglesAppId.WEB_CLIENT);
    }

    @Test
    public void compareApiKeyTest() {
        BearerAuthenticationToken token = userService.createBearerToken(getCurrentUser(), JanglesAppId.WEB_CLIENT);
        BearerTokenKey bearerTokenKey = userService.decodeBearerToken(token);
        assertTrue(isValidApiKey(bearerTokenKey));

        // WEB_CLIENT apiKey, USER_SERVICE appId, will fail
        bearerTokenKey.setAppId(JanglesAppId.USER_SERVICE);
        assertFalse(isValidApiKey(bearerTokenKey));

    }

    @Test
    public void anonymousTokenKeyTest() {
        BearerAuthenticationToken token = userService.createBearerToken(getCurrentUser(), JanglesAppId.WEB_CLIENT);
        assertFalse(userService.isAnonymousToken(token));

        token = userService.createAnonymousToken();
        assertTrue(userService.isAnonymousToken(token));
    }

    @Test
    public void displayRandomUserIdString() {
        assertEquals(25, RandomStringUtils.randomAlphanumeric(25).length());
    }


    // region duplicate private methods from userService

    private Boolean isValidApiKey(BearerTokenKey bearerTokenKey) {
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
        }
        return apiKey;
    }

    // endregion

    // private utility methods

    private CurrentUser getCurrentUser() {
        User user = userService.getUser(BOB);
        return new CurrentUser(user);
    }

    // endregion
}
