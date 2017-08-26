package com.nixmash.postservice.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.postservice.core.UserServiceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by daveburke on 6/19/17.
 */
@RunWith(JUnit4.class)
public class GuiceTest {

    @Inject
    private IConnection iConnection;

    @Inject
    private UserServiceConfig userServiceConfig;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new UserServiceTestModule());
        injector.injectMembers(this);
    }

    @Test
    public void connectionInjectionTest() {
        assertThat(iConnection, is(instanceOf(TestConnection.class)));
    }

    @Test
    public void configInjectionTest() {
        assert(userServiceConfig.applicationId.equals("userservice-test"));
    }
}
