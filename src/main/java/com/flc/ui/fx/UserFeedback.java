package com.flc.ui.fx;

import com.flc.model.Lesson;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Optional;

/**
 * Single place for in-form banners and optional dialogs after user actions (book, change booking, review, etc.).
 * Controllers should not duplicate label style classes or alert wiring.
 */
public final class UserFeedback {

    private static final List<String> BANNER_STYLE_CLASSES = List.of(
            "feedback-success", "feedback-error", "feedback-info");

    private UserFeedback() {}

    /** Clears text and applies neutral banner styling (e.g. on screen open). */
    public static void clear(Label label) {
        applyBanner(label, BannerKind.INFO, "");
    }

    public enum BannerKind {
        SUCCESS,
        ERROR,
        INFO
    }

    /** Sets banner text and success / error / neutral styling. */
    public static void applyBanner(Label label, BannerKind kind, String text) {
        label.setText(text != null ? text : "");
        label.getStyleClass().removeAll(BANNER_STYLE_CLASSES);
        label.getStyleClass().add(switch (kind) {
            case SUCCESS -> "feedback-success";
            case ERROR -> "feedback-error";
            case INFO -> "feedback-info";
        });
    }

    /**
     * Validation or pre-check failure: red banner plus error dialog so the user notices the problem.
     */
    public static void validationIssue(Label label, String bannerText, String alertTitle, String alertMessage) {
        applyBanner(label, BannerKind.ERROR, bannerText);
        FxAlerts.showError(alertTitle, alertMessage != null ? alertMessage : bannerText);
    }

    /**
     * Asks the member to confirm moving a booking from one lesson to another (used before applying the change).
     *
     * @return {@code true} if they confirmed OK
     */
    public static boolean confirmBookingMove(Lesson currentLesson, Lesson newLesson) {
        String from = LessonComboBoxCells.formatLessonLine(currentLesson, true);
        String to = LessonComboBoxCells.formatLessonLine(newLesson, true);
        String body = """
                You are about to change your booking.

                FROM (current lesson):
                  %s

                TO (new lesson):
                  %s

                Apply this change?""".formatted(from, to);
        return FxAlerts.confirm("Confirm booking change", body);
    }

    /**
     * Handles {@code Optional<String>} service errors: present value = error message; empty = success.
     *
     * @param successBannerText shown in green when the service returns no error
     * @param dialogs           which modal dialogs to show (in addition to the banner)
     */
    public static void forOptionalServiceError(
            Label label,
            Optional<String> serviceError,
            String successBannerText,
            ServiceDialogs dialogs) {
        if (serviceError.isPresent()) {
            String msg = serviceError.get();
            applyBanner(label, BannerKind.ERROR, msg);
            if (dialogs.alertOnError()) {
                FxAlerts.showError(dialogs.errorTitle(), msg);
            }
        } else {
            applyBanner(label, BannerKind.SUCCESS, successBannerText);
            if (dialogs.alertOnSuccess()) {
                String body = dialogs.successDetail() != null && !dialogs.successDetail().isBlank()
                        ? dialogs.successDetail()
                        : successBannerText;
                FxAlerts.showInfo(dialogs.successTitle(), body);
            }
        }
    }

    /**
     * Controls optional {@link FxAlerts} for the success and error paths of {@link #forOptionalServiceError}.
     */
    public record ServiceDialogs(
            boolean alertOnError,
            String errorTitle,
            boolean alertOnSuccess,
            String successTitle,
            String successDetail
    ) {
        /** Banner only on success; error banner + error dialog on failure. */
        public static ServiceDialogs errorDialogOnly(String errorTitle) {
            return new ServiceDialogs(true, errorTitle, false, "", null);
        }

        /** Error dialog on failure; information dialog on success (e.g. change booking). */
        public static ServiceDialogs errorAndSuccessDialogs(String errorTitle, String successTitle, String successDetail) {
            return new ServiceDialogs(true, errorTitle, true, successTitle, successDetail);
        }

        /** No modal dialogs — banners only (e.g. book-lesson success). */
        public static ServiceDialogs bannersOnly() {
            return new ServiceDialogs(false, "", false, "", null);
        }
    }
}
