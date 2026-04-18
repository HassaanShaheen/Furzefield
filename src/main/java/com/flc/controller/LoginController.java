package com.flc.controller;

import com.flc.navigation.StageNavigator;
import com.flc.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label successLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signupLink;

    private StageNavigator navigator;
    private AuthService authService;

    public void init(StageNavigator navigator, AuthService authService) {
        this.navigator = navigator;
        this.authService = authService;
        successLabel.setManaged(false);
        successLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
        signupLink.setOnAction(e -> goSignup());
        loginButton.setOnAction(e -> attemptLogin());
        passwordField.setOnAction(e -> attemptLogin());
        Platform.runLater(() -> usernameField.requestFocus());
    }

    public void setFlashSuccess(String message) {
        if (message == null || message.isBlank()) {
            successLabel.setText("");
            successLabel.setManaged(false);
            successLabel.setVisible(false);
            return;
        }
        successLabel.setText(message);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }

    private void attemptLogin() {
        clearError();
        setFlashSuccess(null);
        authService.login(usernameField.getText(), passwordField.getText())
                .ifPresentOrElse(
                        this::showError,
                        () -> {
                            try {
                                navigator.showDashboard(authService.getCurrentUser());
                            } catch (Exception ex) {
                                showError("Could not open dashboard.");
                            }
                        }
                );
    }

    private void goSignup() {
        try {
            navigator.showSignup();
        } catch (Exception ex) {
            showError("Could not open sign up.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
    }
}
