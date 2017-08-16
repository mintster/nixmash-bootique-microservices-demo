package com.nixmash.jangles.db;

import com.google.inject.Inject;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.dto.Role;
import com.nixmash.jangles.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDbImpl extends JanglesSql implements UsersDb {

    IConnection iConnection;

    private static final Logger logger = LoggerFactory.getLogger(UsersDbImpl.class);


    @Inject
    public UsersDbImpl(IConnection iConnection) {
        super(iConnection);
    }


    @Override
    public List<Role> getRoles(Long userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        try {
            ResultSet rs = sqlQuery("SELECT roles.role_id, roles.permission, roles.role_name FROM  user_roles " +
                    "INNER JOIN roles ON user_roles.role_id = roles.role_id WHERE user_roles.user_id ='" + userId + "'");
            while (rs.next()) {
                roles.add(new Role(rs.getInt(1), rs.getString("permission"), rs.getString("role_name")));
            }
        } catch (SQLException | NullPointerException e) {
            logger.info("Error retrieving roles: " + e.getMessage());
        } finally {
            sqlClose();
        }
        return roles;
    }

    @Override
    public User getUser(String username) throws SQLException {
        {
            User user = new User();
            try (CallableStatement cs = sqlCall("SELECT * FROM users WHERE username  = ?")) {
                cs.setString(1, username);
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    user = new User();
                    populateUser(rs, user);
                }
            } catch (SQLException e) {
                logger.info("Error getting user: " + e.getMessage());
            } finally {
                sqlCallClose();
            }
            return user;
        }
    }

    @Override
    public List<User> getUsers() throws SQLException {
        {
            List<User> users = new ArrayList<>();
            ResultSet rs = sqlQuery("SELECT * FROM users ORDER BY user_id");
            User user = null;
            while (rs.next()) {
                user = new User();
                populateUser(rs, user);
                users.add(user);
            }
            sqlClose();
            return users;
        }
    }
    @Override
    public User addUser(User user) throws SQLException {
        try (CallableStatement cs = sqlCall("INSERT INTO users (username, email, first_name, last_name, password) VALUES " +
                "('" + user.getUsername() + "', " +
                "'" + user.getEmail() + "', " +
                "'" + user.getFirstName() + "', " +
                "'" + user.getLastName() + "', " +
                "'" + user.getPassword() + "'" +
                ")")) {
            int result = cs.executeUpdate();
            if (result == 1) {
                ResultSet generatedKeys = cs.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.info("Error creating new user: " + e.getMessage());
        } finally {
            sqlCallClose();
        }
        return user;
    }


    // region Populate List Objects from ResultSets

    private void populateUser(ResultSet rs, User user) throws SQLException {
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEnabled(rs.getBoolean("enabled"));
    }

    // endregion

}
