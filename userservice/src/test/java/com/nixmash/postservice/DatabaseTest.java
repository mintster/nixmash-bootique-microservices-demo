package com.nixmash.postservice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.json.JanglesUser;
import com.nixmash.postservice.guice.UserServiceTestModule;
import com.nixmash.postservice.service.JanglesUserServiceImpl;
import com.nixmash.postservice.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by daveburke on 6/11/17.
 */
@RunWith(JUnit4.class)
public class DatabaseTest {

    // region Properties and Local Variables

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTest.class);

    private  List<JanglesUser> users;

    @Inject
    private JanglesConfiguration janglesConfiguration;

    @Inject
    private JanglesCache janglesCache;

    @Inject
    private JanglesUserServiceImpl userService;

    @Before
    public void setupMethod() {
        Injector injector = Guice.createInjector(new UserServiceTestModule());
        injector.injectMembers(this);
    }

    // endregion

    // region @BeforeClass and @AfterClass

    @BeforeClass
    public static void setupClass(){
        try {
            configureTestDb("populate.sql");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // endregion

    @Test
    public void connectionTest() {
        assertTrue(janglesConfiguration.testDbConnectionName.contains("test"));
    }

    // region JanglesUsers

    @Test
    public void showJanglesUsersLinkTest() {
        List<JanglesUser> janglesUsers = userService.getJanglesUsers();
        for (JanglesUser user : janglesUsers) {
            assertFalse(user.getShowUsersLink());
        }

        JanglesUser janglesUser = userService.getJanglesUser(1L);
        assertTrue(janglesUser.getShowUsersLink());
    }

    @Test
    public void createJanglesUserTest() {
        int usersBefore = userService.getJanglesUsers().size();
        JanglesUser newUser = TestUtils.createJanglesUser("usertest");
        JanglesUser janglesUser = userService.createJanglesUser(newUser);
        assertThat(janglesUser.getUserId(), greaterThan(0L));
        int usersAfter = userService.getJanglesUsers().size();
        assertEquals(usersBefore + 1, usersAfter);
    }

    // endregion
    
    // region Cache Tests
    
    @Test
    @SuppressWarnings("unchecked")
    public void cacheDbRetrievalTest() {
        String key = String.format("JanglesUserList-%s", janglesConfiguration.applicationId);
        users = userService.getJanglesUsers();
        Assertions.assertThat(users.size()).isGreaterThan(0);

        List<JanglesUser> cachedUsers = (List<JanglesUser>) janglesCache.get(key);
        assertEquals(users, cachedUsers);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void clearCacheTest() {
        String key = String.format("JanglesUserList-%s", janglesConfiguration.applicationId);
        users = userService.getJanglesUsers();
        Assertions.assertThat(users.size()).isGreaterThan(0);

        janglesCache.remove(key);

        List<JanglesUser> cachedUsers = (List<JanglesUser>) janglesCache.get(key);
        assertNull(cachedUsers);
    }

    // endregion

}