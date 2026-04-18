package com.flc.ui.fx;

import com.flc.model.Lesson;
import javafx.scene.control.TableView;

/**
 * Lesson tables use computed columns (e.g. spaces left) that do not auto-bind to the booking store.
 * Call {@link #refreshAll(TableView)} after bookings change so counts re-query the repository.
 */
public final class LessonTableRefresh {

    private LessonTableRefresh() {}

    public static void refreshAll(TableView<Lesson> lessonTable) {
        lessonTable.refresh();
    }
}
