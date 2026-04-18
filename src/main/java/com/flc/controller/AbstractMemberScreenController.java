package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.navigation.StageNavigator;
import com.flc.ui.navigation.NavigationSupport;
import javafx.fxml.FXML;

import java.util.Objects;


public abstract class AbstractMemberScreenController {

    protected StageNavigator navigator;
    protected FlcApplicationContext appContext;

    protected final void bindMemberContext(StageNavigator navigator, FlcApplicationContext appContext) {
        this.navigator = Objects.requireNonNull(navigator, "navigator");
        this.appContext = Objects.requireNonNull(appContext, "appContext");
    }

    /** Called from FXML ({@code onAction="#onBack"}) on member feature screens. */
    @FXML
    public void onBack() {
        NavigationSupport.backToDashboard(navigator, appContext);
    }
}
