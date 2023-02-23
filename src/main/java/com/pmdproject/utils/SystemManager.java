package com.pmdproject.utils;

import com.pmdproject.App;
import com.pmdproject.model.Role;
import com.pmdproject.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class SystemManager {
    private static final DBManager dbManager = DBManager.getInstance();

    public static void logUserAction(String description) {
        if (App.getInstance().getUser() != null)
            dbManager.executeUpdate(Query.INSERT_LOG, new String[]{App.getInstance().getUser().getUsername(), description});
    }

    public static boolean login(String username, String password) {
        ResultSet rs = dbManager.executeQuery(Query.GET_USER, new String[]{username});

        try {
            if (rs.next()) {
                if (rs.getBoolean("is_active")) {
                    String hash = rs.getString("passwd");

                    if (Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id).verify(hash, password.toCharArray())) {
                        User user = User.getInstance();
                        user.setUsername(username);

                        if (rs.getBoolean("sel_admin"))
                            user.getRoles().add(Role.ADMIN);
                        if (rs.getBoolean("sel_manager"))
                            user.getRoles().add(Role.MANAGER);
                        if (rs.getBoolean("sel_stockman"))
                            user.getRoles().add(Role.STOCKMAN);
                        if (rs.getBoolean("sel_chef"))
                            user.getRoles().add(Role.CHEF);

                        App.getInstance().setUser(user);
                        rs.close();

                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logout() {
        App.getInstance().deleteUser();
    }

    public static String hashPasswd(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id,
                Argon2Parameters.DEFAULT_SALT_LENGTH, Argon2Parameters.DEFAULT_HASH_LENGTH);

        return argon2.hash(Argon2Parameters.ITERATIONS, Argon2Parameters.MEMORY,
                Argon2Parameters.PARALLELISM, password.toCharArray());
    }
}