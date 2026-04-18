package com.flc.navigation;

import com.flc.MainApp;
import com.flc.controller.LoginController;
import com.flc.controller.MainController;
import com.flc.controller.SignupController;
import com.flc.model.UserAccount;
import com.flc.service.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageNavigator {

    private final Stage stage;
    private final AuthService authService;

    public StageNavigator(Stage stage, AuthService authService) {
        this.stage = Objects.requireNonNull(stage);
        this.authService = Objects.requireNonNull(authService);
    }

    public void showLogin() throws IOException {
        showLogin(null);
    }

    /**
     * @param successMessage optional banner on the sign-in screen (e.g. after registration)
     */
    public void showLogin(String successMessage) throws IOException {
        authService.logout();
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.init(this, authService);
        controller.setFlashSuccess(successMessage);

        Scene scene = new Scene(root, 420, 520);
        applyStyles(scene);
        stage.setTitle("Furzefield Leisure Centre — Sign in");
        stage.setScene(scene);
        stage.setMinWidth(380);
        stage.setMinHeight(480);
        stage.show();
    }

    public void showSignup() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/signup-view.fxml"));
        Parent root = loader.load();
        SignupController controller = loader.getController();
        controller.init(this, authService);

        Scene scene = new Scene(root, 440, 620);
        applyStyles(scene);
        stage.setTitle("Furzefield Leisure Centre — Create account");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(560);
        stage.show();
    }

    public void showDashboard(UserAccount user) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/main-view.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.init(this, authService, user);

        Scene scene = new Scene(root, 1000, 680);
        applyStyles(scene);
        stage.setTitle("Furzefield Leisure Centre — Dashboard");
        stage.setScene(scene);
        stage.setMinWidth(860);
        stage.setMinHeight(600);
        stage.show();
    }

    private static void applyStyles(Scene scene) {
        String css = Objects.requireNonNull(MainApp.class.getResource("/styles/app.css")).toExternalForm();
        scene.getStylesheets().add(css);
    }
}
