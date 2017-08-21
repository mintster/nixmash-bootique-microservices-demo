package com.nixmash.jangles.service;

import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import org.apache.shiro.subject.Subject;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User createUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);
    CurrentUser getCurrentUser(Subject subject);
}
