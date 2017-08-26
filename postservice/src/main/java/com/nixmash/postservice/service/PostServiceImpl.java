package com.nixmash.postservice.service;

import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.core.JanglesConfiguration;
import com.nixmash.jangles.json.JanglesUser;
import com.nixmash.postservice.db.PostsSqlImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    JanglesCache janglesCache;
    JanglesConfiguration janglesConfiguration;
    PostsSqlImpl postsSql;

    @Inject
    public PostServiceImpl(PostsSqlImpl postsSql, JanglesCache janglesCache, JanglesConfiguration janglesConfiguration) {
        this.postsSql = postsSql;
        this.janglesCache = janglesCache;
        this.janglesConfiguration = janglesConfiguration;
    }

    @Override
    public List<JanglesUser> getJanglesUsers() {
        return getJanglesUsers(true);
    }

    @Override
    public List<JanglesUser> getJanglesUsers(boolean useCached) {

        String key = janglesUsersCacheKey();

        @SuppressWarnings("unchecked")
        List<JanglesUser> janglesUsers = (List<JanglesUser>) janglesCache.get(key);
        if (janglesUsers == null || !useCached) {
            try {
                janglesUsers = postsSql.getJanglesUsers();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            janglesCache.put(key, (Serializable) janglesUsers);
        }
        return janglesUsers;
    }

    @Override
    public JanglesUser getJanglesUser(Long userId) {
        JanglesUser janglesUser = null;
        try {
            janglesUser = postsSql.getJanglesUser(userId);
        } catch (SQLException e) {
            logger.error("JanglesUser with id " + userId + " not found!");
            return null;
        }
        janglesUser.setShowUsersLink(true);
        return janglesUser;
    }

    @Override
    public JanglesUser createJanglesUser(JanglesUser janglesUser) {
        Long userId = -1L;
        userId = postsSql.createJanglesUser(janglesUser);
        janglesCache.remove(janglesUsersCacheKey());
        return getJanglesUser(userId);
    }

    private String janglesUsersCacheKey() {
        return String.format("JanglesUserList-%s", janglesConfiguration.applicationId);
    }


}
