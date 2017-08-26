package com.nixmash.postservice.db;

import com.nixmash.jangles.json.JanglesUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by daveburke on 6/22/17.
 */
public interface JanglesUserSql {

    // region Users

    public abstract JanglesUser getJanglesUser(Long userId) throws SQLException;
    public abstract List<JanglesUser> getJanglesUsers() throws SQLException;
    public abstract Long createJanglesUser(JanglesUser janglesUser);

    // endregion
}
