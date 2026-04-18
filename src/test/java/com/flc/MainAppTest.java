package com.flc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainAppTest {

    @Test
    void mainViewResourceExists() {
        assertNotNull(MainApp.class.getResource("/view/main-view.fxml"));
    }

    @Test
    void authViewsAndStylesExist() {
        assertNotNull(MainApp.class.getResource("/view/login-view.fxml"));
        assertNotNull(MainApp.class.getResource("/view/signup-view.fxml"));
        assertNotNull(MainApp.class.getResource("/styles/app.css"));
        assertNotNull(MainApp.class.getResource("/view/timetable-view.fxml"));
        assertNotNull(MainApp.class.getResource("/view/book-lesson-view.fxml"));
        assertNotNull(MainApp.class.getResource("/view/change-booking-view.fxml"));
        assertNotNull(MainApp.class.getResource("/view/review-view.fxml"));
        assertNotNull(MainApp.class.getResource("/view/reports-view.fxml"));
    }
}
