package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.navigation.StageNavigator;
import com.flc.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReportsController extends AbstractMemberScreenController {

    @FXML
    private TextArea reportArea;

    public void init(StageNavigator navigator, FlcApplicationContext appContext) {
        bindMemberContext(navigator, appContext);
        onRefresh();
    }

    @FXML
    private void onRefresh() {
        ReportService reports = appContext.getReportService();
        String r1 = reports.buildReportLessonAttendanceAndRatings();
        String r2 = reports.buildReportHighestIncomeExercise();
        String full = r1 + "\n\n" + r2;
        reportArea.setText(full);
        System.out.println(full);
    }
}
