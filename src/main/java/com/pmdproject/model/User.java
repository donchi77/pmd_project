package com.pmdproject.model;

import java.util.EnumSet;

public class User {
    private static User instance;
    private String username;
    private final EnumSet<Role> roles;

    private User() {
        this.roles = EnumSet.noneOf(Role.class);
    }

    public static synchronized User getInstance() {
        if (instance == null)
            instance = new User();

        return instance;
    }

    public EnumSet<Role> getRoles() {
        return roles;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void deleteInstance() {
        if (instance != null)
            instance = null;
    }
}