package com.pmdproject.model;

import com.pmdproject.utils.DBManager;
import com.pmdproject.utils.Query;
import com.pmdproject.utils.SystemManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

public class AdminManager {
    private final DBManager dbManager;
    public AdminManager() {
        dbManager = DBManager.getInstance();
    }
    public String getLogs() {
        ResultSet rs = dbManager.executeQuery(Query.GET_LOGS);
        StringBuilder sb = new StringBuilder();

        try {
            while (rs.next()) {
                sb.append(rs.getString("username"))
                        .append(" ")
                        .append(rs.getString("date"))
                        .append(" ")
                        .append(rs.getString("descr"))
                        .append("\n");
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public boolean createUser(String username, String password, EnumSet<Role> roles) {
        String hash = SystemManager.hashPasswd(password);
        int rows;

        try {
            PreparedStatement ps = dbManager.getConnection().prepareStatement(Query.INSERT_USER);
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setBoolean(3, roles.contains(Role.ADMIN));
            ps.setBoolean(4, roles.contains(Role.MANAGER));
            ps.setBoolean(5, roles.contains(Role.STOCKMAN));
            ps.setBoolean(6, roles.contains(Role.CHEF));

            rows = dbManager.executeUpdate(ps);
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rows == 1;
    }

    public void deleteUser(String username) {
        dbManager.executeUpdate(Query.UPDATE_ACTIVE_USER, new String[]{username});
    }

    public boolean checkUsername(String username) {
        ResultSet resultSet = dbManager.executeQuery(Query.GET_USERNAME, new String[]{username});
        boolean exists;
        try {
             exists = resultSet.next();
             resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exists;
    }

    public ObservableList<String> getUsernames() {
        ObservableList<String> temp = FXCollections.observableArrayList();
        ResultSet rs = dbManager.executeQuery(Query.GET_ACTIVE_USERS);

        try {
            while (rs.next()) {
                temp.add(rs.getString("username"));
            }
            rs.close();

            return temp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
