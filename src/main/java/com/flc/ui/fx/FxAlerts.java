package com.flc.ui.fx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Simple modal feedback used where a {@link javafx.scene.control.Label} alone is easy to miss.
 */
public final class FxAlerts {

    private FxAlerts() {}

    /** @return {@code true} if the user chose OK, {@code false} for Cancel or close */
    public static boolean confirm(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(contentText);
        a.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
