package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.model.Lesson;
import com.flc.navigation.StageNavigator;
import com.flc.repository.LessonRepository;
import com.flc.service.BookingService;
import com.flc.service.TimetableService;
import com.flc.ui.fx.LessonTableColumns;
import com.flc.ui.fx.LessonTableRefresh;
import com.flc.ui.fx.UserFeedback;
import com.flc.ui.fx.UserFeedback.ServiceDialogs;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Optional;

public class BookLessonController extends AbstractMemberScreenController {

    @FXML
    private TableView<Lesson> lessonTable;
    @FXML
    private Label messageLabel;

    public void init(StageNavigator navigator, FlcApplicationContext appContext) {
        bindMemberContext(navigator, appContext);
        LessonTableColumns.apply(lessonTable, appContext, "Weekend", "Spaces left");
        UserFeedback.clear(messageLabel);
        reloadLessonRows();
    }

    @FXML
    private void onRefresh() {
        reloadLessonRows();
    }

    private void reloadLessonRows() {
        TimetableService ts = appContext.getTimetableService();
        lessonTable.setItems(FXCollections.observableArrayList(ts.allLessonsSorted()));
        LessonTableRefresh.refreshAll(lessonTable);
    }

    @FXML
    private void onBook() {
        Lesson selected = lessonTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UserFeedback.validationIssue(
                    messageLabel,
                    "Please select a lesson in the table.",
                    "No lesson selected",
                    "Click a row in the table, then press Book selected lesson.");
            return;
        }

        LessonRepository lessons = appContext.getLessonRepository();
        Lesson lesson = lessons.findById(selected.getLessonId()).orElse(selected);

        long booked = appContext.getBookingRepository().countByLesson(lesson);
        int spacesLeft = (int) (BookingService.MAX_MEMBERS_PER_LESSON - booked);
        if (spacesLeft <= 0) {
            String msg = "This lesson is full — no spaces left (maximum " + BookingService.MAX_MEMBERS_PER_LESSON + " members).";
            UserFeedback.validationIssue(messageLabel, msg, "Lesson full", msg);
            reloadLessonRows();
            return;
        }

        UserFeedback.clear(messageLabel);
        Optional<String> error = appContext.getBookingService().book(appContext.getCurrentMember(), lesson);
        int remaining = spacesLeft - (error.isEmpty() ? 1 : 0);
        String success = "Lesson booked successfully. Spaces left for this lesson: " + remaining + ".";
        UserFeedback.forOptionalServiceError(messageLabel, error, success, ServiceDialogs.errorDialogOnly("Could not book"));
        reloadLessonRows();
    }
}
