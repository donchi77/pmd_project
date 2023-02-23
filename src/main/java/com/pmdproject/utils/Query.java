package com.pmdproject.utils;

import java.sql.PreparedStatement;

public interface Query {
    String GET_USER = "SELECT passwd, sel_admin, sel_manager, sel_stockman, sel_chef, is_active " +
            "FROM user WHERE username = ?";
    String GET_USERNAME = "SELECT COUNT(username) FROM user WHERE username = ? GROUP BY username";
    String INSERT_LOG = "INSERT INTO log (username, date, descr) VALUES (?, NOW(), ?)";
    String GET_LOGS = "SELECT * FROM log";
    String INSERT_USER = "INSERT INTO user (username, passwd, sel_admin, sel_manager, " +
            "sel_stockman, sel_chef, is_active) VALUES (?, ?, ?, ?, ?, ?, true)";
    String GET_ACTIVE_USERS = "SELECT username FROM user WHERE is_active = true";
    String UPDATE_ACTIVE_USER = "UPDATE user SET is_active = false WHERE username = ?";
}
