package com.nixmash.userservice.service;

import com.google.inject.ImplementedBy;
import com.nixmash.jangles.json.JanglesUser;

import java.util.List;

/**
 * Created by daveburke on 6/18/17.
 */
@ImplementedBy(JanglesUserServiceImpl.class)
public interface JanglesUserService {

    List<JanglesUser> getJanglesUsers();
    List<JanglesUser> getJanglesUsers(boolean useCached);
    JanglesUser getJanglesUser(Long userID);
    JanglesUser createJanglesUser(JanglesUser janglesUser);
}
