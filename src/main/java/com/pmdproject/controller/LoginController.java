package com.pmdproject.controller;

import com.pmdproject.App;
import com.pmdproject.model.Role;
import com.pmdproject.utils.SystemManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import net.synedra.validatorfx.Validator;

public class LoginController extends Controller {
    @FXML
    public Label labelUsername;
    @FXML
    public Label labelPassword;
    @FXML
    public TextField textFieldUsername;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button buttonLogin;
    private final Validator validator = new Validator();

    @Override
    public void initialize() {
        validator.createCheck()
                .dependsOn("username", textFieldUsername.textProperty())
                .withMethod(c -> {
                    String user = c.get("username");
                    if (user.contains("--") || user.contains(";") || user.contains("OR") || user.contains("'"))
                        c.warn("Non provare a fare scherzetti.");
                })
                .decorates(textFieldUsername)
                .immediate();
        validator.createCheck()
                .dependsOn("password", passwordField.textProperty())
                .withMethod(c -> {
                    String passwd = c.get("password");
                    if (passwd.contains("--") || passwd.contains(";") || passwd.contains("OR") || passwd.contains("'"))
                        c.warn("Non provare a fare scherzetti.");
                })
                .decorates(passwordField)
                .immediate();
    }

    @FXML
    public void onButtonLoginClicked() {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();

        if (SystemManager.login(username, password)) {
            if (App.getInstance().getUser().getRoles().contains(Role.ADMIN))
                navigate("admin-view.fxml");
            else
                navigate("home-view.fxml");

            log("Log in the application");
        }
    }
}