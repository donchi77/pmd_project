package com.pmdproject;

import com.pmdproject.controller.Controller;
import com.pmdproject.model.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static App instance;
    private User user;
    private static Scene scene;

    @Override
    public void start(Stage stage) {
        scene = new Scene(loadView("login-view.fxml"), 1280, 800);
        navigate("login-view.fxml");

        stage.setTitle("Gestionale");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setResizable(Boolean.TRUE);
        stage.setScene(scene);
        stage.show();
    }

    public static App getInstance() {
        if (instance == null)
            instance = new App();

        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void deleteUser() {
        this.user.deleteInstance();
        this.user = null;
    }

    public static void navigate(String view) {
        Parent parent = loadView(view);
        scene.setRoot(parent);
    }
    private static Parent loadView(String view) {
        FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource(view));
        Parent parent;

        try {
            parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        Controller controller = loader.getController();
        controller.initialize();

        return parent;
    }

    public static void main(String[] args) {
        launch();
    }
}