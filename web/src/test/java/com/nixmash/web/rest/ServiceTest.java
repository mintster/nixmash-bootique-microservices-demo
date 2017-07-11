package com.nixmash.web.rest;

import com.google.inject.Inject;
import com.nixmash.jangles.model.JanglesUser;
import com.nixmash.web.exceptions.RestProcessingException;
import com.nixmash.web.service.UserClientServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by daveburke on 7/6/17.
 */
@RunWith(JUnit4.class)
public class ServiceTest {

    @Inject
    UserClientServiceImpl mockUserClientService;

    @Before
    public void setup() throws RestProcessingException {
        List<JanglesUser> users = new ArrayList<>();
        mockUserClientService = mock(UserClientServiceImpl.class);
        when(mockUserClientService.getRestUsers()).thenReturn(users);
    }

    @Test
    public void getUsersTest() throws RestProcessingException {
        assertNotNull(mockUserClientService.getRestUsers());
    }

}
