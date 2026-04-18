package com.flc.ui.fx;

import com.flc.model.Lesson;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * Shared {@link ListCell} rendering for {@link Lesson} pickers (combo box popup and button).
 */
public final class LessonComboBoxCells {

    private LessonComboBoxCells() {}

    public static String formatLessonLine(Lesson lesson, boolean includeWeekTag) {
        if (lesson == null) {
            return "";
        }
        String base = lesson.getLessonId() + " — " + lesson.getExerciseType().getDisplayName();
        if (includeWeekTag) {
            return base + " (W" + lesson.getWeekendNumber() + ")";
        }
        return base;
    }

    public static ListCell<Lesson> createListCell(boolean includeWeekTag) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Lesson item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatLessonLine(item, includeWeekTag));
            }
        };
    }

    /**
     * Applies the same display rules to both the dropdown rows and the collapsed button label.
     */
    public static void configure(ComboBox<Lesson> combo) {
        combo.setCellFactory(lv -> createListCell(true));
        combo.setButtonCell(createListCell(false));
    }
}
