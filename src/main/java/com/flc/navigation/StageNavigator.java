package com.flc.navigation;

import com.flc.FlcApplicationContext;
import com.flc.MainApp;
import com.flc.controller.BookLessonController;
import com.flc.controller.ChangeBookingController;
import com.flc.controller.LoginController;
import com.flc.controller.MainController;
import com.flc.controller.ReportsController;
import com.flc.controller.ReviewController;
import com.flc.controller.SignupController;
import com.flc.controller.TimetableController;
import com.flc.model.Member;
import com.flc.model.UserAccount;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class StageNavigator {

    /** Shared minimum for dashboard and member feature screens so the window never “steps down” on navigation. */
    private static final double MEMBER_MIN_WIDTH = 860;
    private static final double MEMBER_MIN_HEIGHT = 600;

    private final Stage stage;
    private final FlcApplicationContext appContext;

    public StageNavigator(Stage stage, FlcApplicationContext appContext) {
        this.stage = Objects.requireNonNull(stage);
        this.appContext = Objects.requireNonNull(appContext);
    }

    public FlcApplicationContext getAppContext() {
        return appContext;
    }

    public void showLogin() throws IOException {
        showLogin(null);
    }

    public void showLogin(String successMessage) throws IOException {
        appContext.clearSession();
        appContext.getAuthService().logout();
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.init(this, appContext);
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
        controller.init(this, appContext);

        Scene scene = new Scene(root, 440, 620);
        applyStyles(scene);
        stage.setTitle("Furzefield Leisure Centre — Create account");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(560);
        stage.show();
    }

    public void showDashboard(UserAccount user) throws IOException {
        Member member = appContext.resolveMemberForUser(user);
        appContext.setSession(user, member);

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/main-view.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.init(this, appContext, user);

        switchRootPreservingStageBounds(
                root,
                "Furzefield Leisure Centre — Dashboard",
                1000,
                680,
                MEMBER_MIN_WIDTH,
                MEMBER_MIN_HEIGHT);
    }

    public void showTimetable() throws IOException {
        loadFeatureScene("/view/timetable-view.fxml", c -> ((TimetableController) c).init(this, appContext),
                "Timetable", 960, 640);
    }

    public void showBookLesson() throws IOException {
        loadFeatureScene("/view/book-lesson-view.fxml", c -> ((BookLessonController) c).init(this, appContext),
                "Book a lesson", 960, 640);
    }

    public void showChangeBooking() throws IOException {
        loadFeatureScene("/view/change-booking-view.fxml", c -> ((ChangeBookingController) c).init(this, appContext),
                "Change booking", 960, 680);
    }

    public void showReview() throws IOException {
        loadFeatureScene("/view/review-view.fxml", c -> ((ReviewController) c).init(this, appContext),
                "Add review", 900, 620);
    }

    public void showReports() throws IOException {
        loadFeatureScene("/view/reports-view.fxml", c -> ((ReportsController) c).init(this, appContext),
                "Reports", 900, 700);
    }

    private void loadFeatureScene(String resource, Consumer<Object> init, String title, double w, double h) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(resource));
        Parent root = loader.load();
        init.accept(loader.getController());

        switchRootPreservingStageBounds(
                root,
                "Furzefield Leisure Centre — " + title,
                w,
                h,
                MEMBER_MIN_WIDTH,
                MEMBER_MIN_HEIGHT);
    }

    /**
     * Keeps the stage width and height (and maximized state) when swapping scenes so a smaller
     * default scene size does not shrink the window after the user enlarged or maximized it.
     */
    private void switchRootPreservingStageBounds(
            Parent root,
            String title,
            double defaultWidth,
            double defaultHeight,
            double minWidth,
            double minHeight) {
        boolean maximized = stage.isMaximized();
        boolean fullScreen = stage.isFullScreen();
        double cw = stage.getWidth();
        double ch = stage.getHeight();

        double w = defaultWidth;
        double h = defaultHeight;
        if (fullScreen || maximized) {
            if (cw >= minWidth && ch >= minHeight && !Double.isNaN(cw) && !Double.isNaN(ch)) {
                w = cw;
                h = ch;
            }
        } else if (cw >= minWidth && ch >= minHeight && !Double.isNaN(cw) && !Double.isNaN(ch)) {
            w = cw;
            h = ch;
        }

        w = Math.max(w, minWidth);
        h = Math.max(h, minHeight);

        Scene scene = new Scene(root, w, h);
        applyStyles(scene);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        if (fullScreen) {
            Platform.runLater(() -> stage.setFullScreen(true));
        } else if (maximized) {
            Platform.runLater(() -> stage.setMaximized(true));
        } else {
            stage.setWidth(w);
            stage.setHeight(h);
        }
    }

    private static void applyStyles(Scene scene) {
        String css = Objects.requireNonNull(MainApp.class.getResource("/styles/app.css")).toExternalForm();
        scene.getStylesheets().add(css);
    }
}
