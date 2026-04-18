package com.flc.ui.fx;

import com.flc.FlcApplicationContext;
import com.flc.model.Lesson;
import com.flc.service.BookingService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the standard {@link Lesson} table columns used on timetable, book, and change-booking screens.
 */
public final class LessonTableColumns {

    private LessonTableColumns() {}

    /**
     * @param weekendHeader e.g. {@code "Weekend"} or {@code "Wk"}
     * @param spacesHeader  e.g. {@code "Spaces left"} or {@code "Spaces"}
     */
    public static void apply(
            TableView<Lesson> table,
            FlcApplicationContext ctx,
            String weekendHeader,
            String spacesHeader) {
        List<TableColumn<Lesson, ?>> columns = new ArrayList<>();

        TableColumn<Lesson, String> idCol = new TableColumn<>("Lesson ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        idCol.setPrefWidth(200);
        columns.add(idCol);

        TableColumn<Lesson, Integer> weekCol = new TableColumn<>(weekendHeader);
        weekCol.setCellValueFactory(new PropertyValueFactory<>("weekendNumber"));
        weekCol.setPrefWidth("Wk".equals(weekendHeader) ? 50 : 80);
        columns.add(weekCol);

        TableColumn<Lesson, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getDay().name()));
        dayCol.setPrefWidth(90);
        columns.add(dayCol);

        TableColumn<Lesson, String> slotCol = new TableColumn<>("Slot");
        slotCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getTimeSlot().name()));
        slotCol.setPrefWidth(100);
        columns.add(slotCol);

        TableColumn<Lesson, String> exCol = new TableColumn<>("Exercise");
        exCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                cd.getValue().getExerciseType().getDisplayName()));
        exCol.setPrefWidth(120);
        columns.add(exCol);

        TableColumn<Lesson, Double> priceCol = new TableColumn<>("Price £");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);
        columns.add(priceCol);

        TableColumn<Lesson, Integer> spacesCol = new TableColumn<>(spacesHeader);
        spacesCol.setCellValueFactory(cd -> {
            Lesson l = cd.getValue();
            int left = (int) (BookingService.MAX_MEMBERS_PER_LESSON
                    - ctx.getBookingRepository().countByLesson(l));
            return new ReadOnlyObjectWrapper<>(left);
        });
        spacesCol.setPrefWidth(100);
        columns.add(spacesCol);

        table.getColumns().setAll(columns);
    }
}
