package com.nixmash.jangles.db;

import com.google.inject.ImplementedBy;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;

import java.sql.SQLException;
import java.util.List;

@ImplementedBy(UsersDbImpl.class)
public interface UsersDb {

    List<User> getUsers() throws SQLException;
    User addUser(User user) throws SQLException;
    User getUser(String username) throws SQLException;
    List<Role> getRoles(Long userId) throws SQLException;
    User getUserByUserKey(String userKey) throws SQLException;
}
