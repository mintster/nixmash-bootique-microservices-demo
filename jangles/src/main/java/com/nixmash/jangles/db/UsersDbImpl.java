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
            ResultSet rs = sqlQuery("SELECT r.role_id, r.permission, r.role_name " +
                    "FROM  user_roles ur  INNER JOIN roles r ON ur.role_id = r.role_id " +
                    "WHERE ur.user_id ='" + userId + "'");
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
            try (CallableStatement cs = sqlCall("SELECT * FROM v_users WHERE username  = ?")) {
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
            ResultSet rs = sqlQuery("SELECT * FROM v_users ORDER BY user_id");
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
                sqlCall("INSERT INTO user_data (user_id) VALUES (" + user.getUserId() + ")").execute();
            }
        } catch (SQLException e) {
            logger.info("Error creating new user: " + e.getMessage());
        } finally {
            sqlCallClose();
        }
        return user;
    }

    @Override
    public User getUserByUserKey(String userKey) throws SQLException {
        {
            User user = new User();
            try {
                CallableStatement cs = sqlCall("SELECT * FROM v_users WHERE user_key = ?");
                cs.setString(1, userKey);
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    user = new User();
                    populateUser(rs, user);
                }
                rs.close();
                cs.close();
            } catch (SQLException e) {
                logger.info("Error getting user by UserKey: " + e.getMessage());
            } finally {
                sqlCallClose();
            }
            return user;
        }
    }

    // region Populate


    public void populateUser(ResultSet rs, User user)
            throws SQLException {
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setAccountExpired(rs.getBoolean("account_expired"));
        user.setAccountLocked(rs.getBoolean("account_locked"));
        user.setCredentialsExpired(rs.getBoolean("credentials_expired"));
        user.setHasAvatar(rs.getBoolean("has_avatar"));
        user.setUserKey(rs.getString("user_key"));
        user.setProviderId(rs.getString("provider_id"));
        user.setPassword(rs.getString("password"));
        user.setVersion(rs.getInt("version"));
        user.setLoginAttempts(rs.getInt("login_attempts"));
        user.setLastloginDatetime(rs.getTimestamp("lastlogin_datetime"));
        user.setCreatedDatetime(rs.getTimestamp("created_datetime"));
        user.setApprovedDatetime(rs.getTimestamp("approved_datetime"));
        user.setInvitedDatetime(rs.getTimestamp("invited_datetime"));
        user.setAcceptedDatetime(rs.getTimestamp("accepted_datetime"));
        user.setInvitedById(rs.getLong("invited_by_id"));
        user.setIp(rs.getString("ip"));

    }
    // endregion

}
