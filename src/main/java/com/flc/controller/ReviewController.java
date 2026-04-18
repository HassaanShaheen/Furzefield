package com.flc.controller;

import com.flc.FlcApplicationContext;
import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.model.Review;
import com.flc.navigation.StageNavigator;
import com.flc.ui.fx.LessonComboBoxCells;
import com.flc.ui.fx.UserFeedback;
import com.flc.ui.fx.UserFeedback.ServiceDialogs;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReviewController extends AbstractMemberScreenController {

    @FXML
    private ComboBox<Lesson> lessonCombo;
    @FXML
    private Spinner<Integer> ratingSpinner;
    @FXML
    private TextArea commentArea;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Review> reviewsTable;

    public void init(StageNavigator navigator, FlcApplicationContext appContext) {
        bindMemberContext(navigator, appContext);
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5));
        commentArea.setText("");
        UserFeedback.clear(messageLabel);
        LessonComboBoxCells.configure(lessonCombo);
        ensureReviewColumns();
        refreshEligibleLessons();
        refreshMyReviewsTable();
    }

    private void ensureReviewColumns() {
        if (!reviewsTable.getColumns().isEmpty()) {
            return;
        }
        TableColumn<Review, String> lessonCol = new TableColumn<>("Lesson");
        lessonCol.setPrefWidth(280);
        lessonCol.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(LessonComboBoxCells.formatLessonLine(cd.getValue().getLesson(), true)));
        TableColumn<Review, Integer> ratingCol = new TableColumn<>("Rating");
        ratingCol.setPrefWidth(64);
        ratingCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getRating()));
        TableColumn<Review, String> commentCol = new TableColumn<>("Comment");
        commentCol.setPrefWidth(340);
        commentCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getComment()));
        reviewsTable.getColumns().addAll(lessonCol, ratingCol, commentCol);
    }

    private void refreshMyReviewsTable() {
        reviewsTable.setItems(FXCollections.observableArrayList(
                appContext.getReviewRepository().findByMember(appContext.getCurrentMember())));
    }

    private void refreshEligibleLessons() {
        Set<Lesson> eligible = new LinkedHashSet<>();
        for (Booking b : appContext.getBookingService().listBookingsForMember(appContext.getCurrentMember())) {
            Lesson l = b.getLesson();
            if (!appContext.getReviewRepository().hasReview(appContext.getCurrentMember(), l)) {
                eligible.add(l);
            }
        }
        lessonCombo.setItems(FXCollections.observableArrayList(eligible));
        if (!eligible.isEmpty()) {
            lessonCombo.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void onSubmit() {
        Lesson lesson = lessonCombo.getSelectionModel().getSelectedItem();
        if (lesson == null) {
            UserFeedback.validationIssue(
                    messageLabel,
                    "No lesson selected — book a lesson first, or you have reviewed all booked lessons.",
                    "Cannot submit review",
                    "Choose a lesson from the list, or go back and book a lesson you attended.");
            return;
        }

        UserFeedback.clear(messageLabel);
        int rating = ratingSpinner.getValue();
        String comment = commentArea.getText();

        var result = appContext.getReviewService().addReview(appContext.getCurrentMember(), lesson, rating, comment);
        UserFeedback.forOptionalServiceError(
                messageLabel,
                result,
                "Thank you — your review was saved.",
                ServiceDialogs.errorDialogOnly("Review not saved"));

        if (result.isEmpty()) {
            commentArea.clear();
            refreshEligibleLessons();
            refreshMyReviewsTable();
        }
    }
}
