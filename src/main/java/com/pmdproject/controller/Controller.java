package com.pmdproject.controller;

import com.pmdproject.App;
import com.pmdproject.model.User;
import com.pmdproject.utils.SystemManager;

public abstract class Controller {
    public void initialize() {}

    public void navigate(String view) {
        App.navigate(view);
    }

    protected void log(String description) {
        SystemManager.logUserAction(description);
    }

    protected User getUser() {
        return App.getInstance().getUser();
    }

    protected void logout() {
        log("Log out from application");
        SystemManager.logout();
        navigate("login-view.fxml");
    }
}
