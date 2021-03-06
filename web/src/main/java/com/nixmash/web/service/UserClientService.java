package com.nixmash.web.service;

import com.google.inject.ImplementedBy;
import com.nixmash.jangles.json.JanglesUser;
import com.nixmash.web.exceptions.RestProcessingException;

import java.util.List;

/**
 * Created by daveburke on 7/5/17.
 */
@ImplementedBy(UserClientServiceImpl.class)
public interface UserClientService {

    @SuppressWarnings("unchecked")
    String performTokenCheck();

    List<JanglesUser> getRestUsers() throws RestProcessingException;

}
