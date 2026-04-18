package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.navigation.StageNavigator;
import com.flc.service.TimetableService;
import com.flc.ui.fx.BookingTableColumns;
import com.flc.ui.fx.LessonComboBoxCells;
import com.flc.ui.fx.LessonTableColumns;
import com.flc.ui.fx.LessonTableRefresh;
import com.flc.ui.fx.UserFeedback;
import com.flc.ui.fx.UserFeedback.BannerKind;
import com.flc.ui.fx.UserFeedback.ServiceDialogs;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Optional;

public class ChangeBookingController extends AbstractMemberScreenController {

    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableView<Lesson> lessonTable;
    @FXML
    private Label messageLabel;

    public void init(StageNavigator navigator, FlcApplicationContext appContext) {
        bindMemberContext(navigator, appContext);
        BookingTableColumns.applySummary(bookingTable);
        LessonTableColumns.apply(lessonTable, appContext, "Wk", "Spaces");
        UserFeedback.clear(messageLabel);
        onRefresh();
    }

    @FXML
    private void onRefresh() {
        bookingTable.setItems(FXCollections.observableArrayList(
                appContext.getBookingService().listBookingsForMember(appContext.getCurrentMember())));
        TimetableService ts = appContext.getTimetableService();
        lessonTable.setItems(FXCollections.observableArrayList(ts.allLessonsSorted()));
        LessonTableRefresh.refreshAll(lessonTable);
    }

    @FXML
    private void onApply() {
        Booking b = bookingTable.getSelectionModel().getSelectedItem();
        Lesson target = lessonTable.getSelectionModel().getSelectedItem();
        if (b == null || target == null) {
            UserFeedback.validationIssue(
                    messageLabel,
                    "Select both a booking and a target lesson.",
                    "Selection needed",
                    "Select a booking in the first table and a target lesson in the second.");
            return;
        }

        Lesson currentLesson = b.getLesson();
        if (currentLesson.getLessonId().equals(target.getLessonId())) {
            UserFeedback.validationIssue(
                    messageLabel,
                    "The target lesson is the same as your current booking — pick a different lesson.",
                    "No change needed",
                    "Choose another row in \"Move to this lesson\" that is not the same as your current lesson.");
            return;
        }

        if (!UserFeedback.confirmBookingMove(currentLesson, target)) {
            UserFeedback.applyBanner(
                    messageLabel,
                    BannerKind.INFO,
                    "Change cancelled — your booking was not modified. You can adjust your selection and try again.");
            return;
        }

        UserFeedback.clear(messageLabel);
        Optional<String> error = appContext.getBookingService()
                .changeBooking(b.getBookingId(), appContext.getCurrentMember(), target);

        String fromLine = LessonComboBoxCells.formatLessonLine(currentLesson, true);
        String toLine = LessonComboBoxCells.formatLessonLine(target, true);
        String successBanner = "Done: your booking was moved from "
                + fromLine
                + " to "
                + toLine
                + ". The tables below are refreshed — check \"Your bookings\" for your updated lesson (booking ID may change).";

        String successDialogDetail = "FROM: " + fromLine + "\n\nTO: " + toLine
                + "\n\nThe \"Your bookings\" table and lesson spaces have been updated so you can verify the change.";

        UserFeedback.forOptionalServiceError(
                messageLabel,
                error,
                successBanner,
                ServiceDialogs.errorAndSuccessDialogs(
                        "Could not change booking",
                        "Booking change completed",
                        successDialogDetail));

        onRefresh();
    }
}
