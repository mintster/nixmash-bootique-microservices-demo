package com.nixmash.userservice.db;

import com.google.inject.Inject;
import com.nixmash.jangles.db.cn.IConnection;
import com.nixmash.jangles.db.JanglesSql;
import com.nixmash.jangles.json.JanglesUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JanglesUserSqlImpl extends JanglesSql implements JanglesUserSql {

    IConnection iConnection;

    private static final Logger logger = LoggerFactory.getLogger(JanglesUserSqlImpl.class);


    @Inject
    public JanglesUserSqlImpl(IConnection iConnection) {
        super(iConnection);
    }

    // region janglesUsers

    @Override
    public JanglesUser getJanglesUser(Long userId) throws SQLException {
        {
            CallableStatement cs = sqlCall("SELECT * FROM jangles_users WHERE user_id  = ?");
            cs.setLong(1, userId);
            ResultSet rs = cs.executeQuery();

            JanglesUser janglesUser = null;
            while (rs.next()) {
                janglesUser = new JanglesUser();
                populateJanglesUser(rs, janglesUser);
            }
            sqlCallClose();
            return janglesUser;
        }
    }

    @Override
    public List<JanglesUser> getJanglesUsers() throws SQLException {
        {
            List<JanglesUser> janglesUserList = new ArrayList<>();
            ResultSet rs = sqlQuery("SELECT * FROM jangles_users ORDER BY user_id");
            JanglesUser janglesUser = null;
            while (rs.next()) {
                janglesUser = new JanglesUser();
                populateJanglesUser(rs, janglesUser);
                janglesUserList.add(janglesUser);
            }
            sqlClose();
            return janglesUserList;
        }
    }

    @Override
    public Long createJanglesUser(JanglesUser janglesUser) {
        Long userId = -1L;
        try (CallableStatement cs = sqlCall("INSERT INTO jangles_users (username, password, display_name, is_active) VALUES " +
                "('" + janglesUser.getUserName() + "', " +
                "'" + janglesUser.getPassword() + "', " +
                "'" + janglesUser.getDisplayName() + "', " +
                janglesUser.getIsActive() + ")")) {
            int result = cs.executeUpdate();
            if (result == 1) {
                ResultSet generatedKeys = cs.getGeneratedKeys();
                if (generatedKeys.next()) {
                    janglesUser.setUserId(generatedKeys.getLong(1));
                }
            }
            cs.close();

        } catch (SQLException e) {
            logger.info("Error creating new user: " + e.getMessage());
        }
        return janglesUser.getUserId();
    }

    // endregion

    // region Populate List Objects from ResultSets

    private void populateJanglesUser(ResultSet rs, JanglesUser janglesUser) throws SQLException {
        janglesUser.setUserId(rs.getLong("user_id"));
        janglesUser.setUserName(rs.getString("username"));
        janglesUser.setPassword(rs.getString("password"));
        janglesUser.setDisplayName(rs.getString("display_name"));
        janglesUser.setDateCreated(rs.getTimestamp("date_created"));
        janglesUser.setIsActive(rs.getBoolean("is_active"));
    }

    // endregion

}
