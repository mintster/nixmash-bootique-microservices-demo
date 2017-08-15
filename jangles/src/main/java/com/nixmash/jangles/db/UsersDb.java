package com.nixmash.jangles.db;

import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;

import java.sql.SQLException;
import java.util.List;

public interface UsersDb {

    User addUser(User user) throws SQLException;
    User getUser(String username) throws SQLException;
    List<Role> getRoles(Long userId) throws SQLException;
}
