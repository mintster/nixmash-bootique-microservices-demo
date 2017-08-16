package com.nixmash.jangles.db;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.guice.JanglesTestModule;
import com.nixmash.jangles.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static com.nixmash.jangles.utils.JanglesUtils.configureTestDb;

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
}
