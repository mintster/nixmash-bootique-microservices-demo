package com.nixmash.userservice.service;

import com.google.inject.ImplementedBy;
import com.nixmash.jangles.model.JanglesUser;

import java.util.List;

/**
 * Created by daveburke on 6/18/17.
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    List<JanglesUser> getJanglesUsers();
    List<JanglesUser> getJanglesUsers(boolean useCached);
    JanglesUser getJanglesUser(Long userID);
    JanglesUser createJanglesUser(JanglesUser janglesUser);
}
