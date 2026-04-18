package com.flc.controller;

import com.flc.navigation.StageNavigator;
import com.flc.service.AuthService;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField displayNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button signupButton;
    @FXML
    private Hyperlink loginLink;

    private StageNavigator navigator;
    private AuthService authService;

    public void init(StageNavigator navigator, AuthService authService) {
        this.navigator = navigator;
        this.authService = authService;
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
        loginLink.setOnAction(e -> goLogin());
        signupButton.setOnAction(e -> attemptSignup());
        Platform.runLater(() -> usernameField.requestFocus());
    }

    private void attemptSignup() {
        clearError();
        Optional<String> regError = authService.register(
                usernameField.getText(),
                displayNameField.getText(),
                passwordField.getText(),
                confirmPasswordField.getText()
        );
        if (regError.isPresent()) {
            showError(regError.get());
            return;
        }
        try {
            navigator.showLogin("Account created. Please sign in with your new username and password.");
        } catch (Exception ex) {
            showError("Account created but could not open sign in. Please try again.");
        }
    }

    private void goLogin() {
        try {
            navigator.showLogin();
        } catch (Exception ex) {
            showError("Could not return to sign in.");
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
