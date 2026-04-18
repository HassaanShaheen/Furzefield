package com.flc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainAppTest {

    @Test
    void mainViewResourceExists() {
        assertNotNull(MainApp.class.getResource("/view/main-view.fxml"));
    }
}
