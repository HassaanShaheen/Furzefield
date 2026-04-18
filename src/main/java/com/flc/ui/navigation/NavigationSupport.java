package com.flc.ui.navigation;

import com.flc.FlcApplicationContext;
import com.flc.model.UserAccount;
import com.flc.navigation.StageNavigator;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Central place for navigation actions used from multiple controllers.
 */
public final class NavigationSupport {

    private static final Logger LOG = Logger.getLogger(NavigationSupport.class.getName());

    private NavigationSupport() {}

    /**
     * Returns to the member dashboard after a feature screen. Failures are logged (no UI here).
     */
    public static void backToDashboard(StageNavigator navigator, FlcApplicationContext appContext) {
        Objects.requireNonNull(navigator, "navigator");
        Objects.requireNonNull(appContext, "appContext");
        UserAccount user = appContext.getCurrentUser();
        if (user == null) {
            LOG.warning("Cannot navigate to dashboard: no current user in context.");
            return;
        }
        try {
            navigator.showDashboard(user);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Failed to open dashboard", e);
        }
    }

    /**
     * Opens the dashboard for the given user (e.g. immediately after login). Returns {@code false} on failure.
     */
    public static boolean tryOpenDashboard(StageNavigator navigator, UserAccount user) {
        Objects.requireNonNull(navigator, "navigator");
        Objects.requireNonNull(user, "user");
        try {
            navigator.showDashboard(user);
            return true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Failed to open dashboard", e);
            return false;
        }
    }
}
