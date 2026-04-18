package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.model.Lesson;
import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;
import com.flc.navigation.StageNavigator;
import com.flc.service.TimetableService;
import com.flc.ui.fx.LessonTableColumns;
import com.flc.ui.fx.LessonTableRefresh;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.List;

public class TimetableController extends AbstractMemberScreenController {

    @FXML
    private ChoiceBox<String> filterModeChoice;
    @FXML
    private ComboBox<Day> dayCombo;
    @FXML
    private ComboBox<ExerciseType> exerciseCombo;
    @FXML
    private TableView<Lesson> lessonTable;
    @FXML
    private Label hintLabel;

    public void init(StageNavigator navigator, FlcApplicationContext appContext) {
        bindMemberContext(navigator, appContext);

        filterModeChoice.setItems(FXCollections.observableArrayList("By day", "By exercise name"));
        filterModeChoice.getSelectionModel().selectFirst();
        dayCombo.setItems(FXCollections.observableArrayList(Day.values()));
        dayCombo.getSelectionModel().selectFirst();
        exerciseCombo.setItems(FXCollections.observableArrayList(ExerciseType.values()));
        exerciseCombo.getSelectionModel().selectFirst();

        filterModeChoice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateFilterVisibility());
        updateFilterVisibility();

        LessonTableColumns.apply(lessonTable, appContext, "Weekend", "Spaces left");
        onApplyFilter();
    }

    private void updateFilterVisibility() {
        String mode = filterModeChoice.getSelectionModel().getSelectedItem();
        boolean byDay = "By day".equals(mode);
        dayCombo.setVisible(byDay);
        dayCombo.setManaged(byDay);
        exerciseCombo.setVisible(!byDay);
        exerciseCombo.setManaged(!byDay);
        hintLabel.setText(byDay
                ? "Shows all lessons on the selected day (all weekends)."
                : "Shows all lessons for the selected exercise type.");
    }

    @FXML
    private void onApplyFilter() {
        TimetableService ts = appContext.getTimetableService();
        String mode = filterModeChoice.getSelectionModel().getSelectedItem();
        List<Lesson> rows;
        if ("By day".equals(mode)) {
            Day d = dayCombo.getSelectionModel().getSelectedItem();
            rows = ts.filterByDay(d);
        } else {
            ExerciseType t = exerciseCombo.getSelectionModel().getSelectedItem();
            rows = ts.filterByExerciseType(t);
        }
        lessonTable.setItems(FXCollections.observableArrayList(rows));
        LessonTableRefresh.refreshAll(lessonTable);
    }
}
