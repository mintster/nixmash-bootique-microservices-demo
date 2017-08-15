package com.nixmash.web.service;

import com.nixmash.jangles.dto.CurrentUser;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import org.apache.shiro.subject.Subject;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);
    CurrentUser createCurrentUser(Subject subject);
}
