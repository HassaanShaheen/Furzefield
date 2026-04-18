package com.flc;

import com.flc.navigation.StageNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FlcApplicationContext appContext = FlcApplicationContext.createWithSampleData();
        StageNavigator navigator = new StageNavigator(stage, appContext);
        navigator.showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}
