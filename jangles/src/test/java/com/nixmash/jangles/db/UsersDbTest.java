package com.nixmash.jangles.db;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.dto.User;
import com.nixmash.jangles.guice.JanglesTestModule;
import com.nixmash.jangles.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class UsersDbTest {

    @Inject
    private UserService userService;

    // region @BeforeClass and @AfterClass

    @BeforeClass
    public static void setup(){
        try {
            configureTestDb("populate.sql");
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void injectObjects() {
        Injector injector = Guice.createInjector(new JanglesTestModule());
        injector.injectMembers(this);
    }

    // endregion

    @Test
    public void getRolesTest() throws SQLException {
        Assert.assertNotNull(userService.getRoles(1L));
    }

    @Test
    public void getUsersTest() throws SQLException {
        Assert.assertNotNull(userService.getUsers());
    }

    @Test
    public void newUserAddsTotalByOneTest() throws Exception {
        User jammer = new User("jammer", "jammer@aol.com", "Jammer", "McGee","password");
        User saved = userService.createUser(jammer);

        User retrieved = userService.getUser("jammer");
        assertThat(saved.getUserId().intValue(), greaterThan(0));
        Assert.assertEquals(saved.getUserId(), retrieved.getUserId());
    }

    @Test
    public void userHasCreatedDateTest() throws Exception {
        User retrieved = userService.getUser("bob");
        Assert.assertNotNull(retrieved.getCreatedDatetime());
    }
}
