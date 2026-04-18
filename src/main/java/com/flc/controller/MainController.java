package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.model.UserAccount;
import com.flc.navigation.StageNavigator;
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
    @FXML
    private Button timetableButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button changeBookingButton;
    @FXML
    private Button reviewButton;
    @FXML
    private Button reportsButton;

    private StageNavigator navigator;
    private FlcApplicationContext appContext;

    public void init(StageNavigator navigator, FlcApplicationContext appContext, UserAccount user) {
        this.navigator = navigator;
        this.appContext = appContext;
        welcomeLabel.setText("Welcome, " + user.getDisplayName());
        subtitleLabel.setText("Signed in as @" + user.getUsername());
        logoutButton.setOnAction(e -> logout());
        timetableButton.setOnAction(e -> go(navigator::showTimetable));
        bookButton.setOnAction(e -> go(navigator::showBookLesson));
        changeBookingButton.setOnAction(e -> go(navigator::showChangeBooking));
        reviewButton.setOnAction(e -> go(navigator::showReview));
        reportsButton.setOnAction(e -> go(navigator::showReports));
    }

    private void go(NavigationAction action) {
        try {
            action.run();
        } catch (Exception ex) {
            welcomeLabel.setText("Could not open screen: " + ex.getMessage());
        }
    }

    @FunctionalInterface
    private interface NavigationAction {
        void run() throws Exception;
    }

    private void logout() {
        appContext.getAuthService().logout();
        try {
            navigator.showLogin();
        } catch (Exception ex) {
            welcomeLabel.setText("Could not sign out. Please restart the app.");
        }
    }
}
