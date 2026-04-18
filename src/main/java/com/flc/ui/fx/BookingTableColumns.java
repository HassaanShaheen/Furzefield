package com.flc.ui.fx;

import com.flc.model.Booking;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard columns for listing a member's {@link Booking} rows (e.g. change-booking screen).
 */
public final class BookingTableColumns {

    private BookingTableColumns() {}

    public static void applySummary(TableView<Booking> table) {
        List<TableColumn<Booking, ?>> columns = new ArrayList<>();

        TableColumn<Booking, String> bid = new TableColumn<>("Booking ID");
        bid.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bid.setPrefWidth(280);
        columns.add(bid);

        TableColumn<Booking, String> lid = new TableColumn<>("Lesson");
        lid.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getLesson().getLessonId()));
        columns.add(lid);

        TableColumn<Booking, String> ex = new TableColumn<>("Exercise");
        ex.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                cd.getValue().getLesson().getExerciseType().getDisplayName()));
        columns.add(ex);

        table.getColumns().setAll(columns);
    }
}
