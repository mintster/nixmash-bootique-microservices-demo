package com.nixmash.jangles;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.guice.JanglesTestModule;
import com.nixmash.jangles.guice.TestApiServiceImpl;
import com.nixmash.jangles.guice.TestConnection;
import com.nixmash.jangles.service.JanglesApiService;
import org.assertj.core.api.Assertions;
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
    private JanglesApiService janglesApiService;

    @Inject
    private IConnection iConnection;

    @Inject
    private JanglesConfiguration janglesConfiguration;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new JanglesTestModule());
        injector.injectMembers(this);
    }

    @Test
    public void apiInjectionTest() {
        assertThat(janglesApiService, is(instanceOf(TestApiServiceImpl.class)));
    }

    @Test
    public void connectionInjectionTest() {
        assertThat(iConnection, is(instanceOf(TestConnection.class)));
    }

    @Test
    public void configurationTest() {
        String globalPropertiesFile = janglesConfiguration.globalPropertiesFile;
        Assertions.assertThat(globalPropertiesFile.endsWith("global.properties"));
    }

}
