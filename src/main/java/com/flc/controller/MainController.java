package com.flc.controller;

import com.flc.navigation.StageNavigator;
import com.flc.model.UserAccount;
import com.flc.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Button logoutButton;

    private StageNavigator navigator;
    private AuthService authService;

    public void init(StageNavigator navigator, AuthService authService, UserAccount user) {
        this.navigator = navigator;
        this.authService = authService;
        welcomeLabel.setText("Welcome, " + user.getDisplayName());
        subtitleLabel.setText("Signed in as @" + user.getUsername());
        logoutButton.setOnAction(e -> logout());
    }

    private void logout() {
        authService.logout();
        try {
            navigator.showLogin();
        } catch (Exception ex) {
            welcomeLabel.setText("Could not sign out. Please restart the app.");
        }
    }
}
