package com.flc;

import com.flc.navigation.StageNavigator;
import com.flc.service.AuthService;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AuthService authService = new AuthService();
        StageNavigator navigator = new StageNavigator(stage, authService);
        navigator.showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}
