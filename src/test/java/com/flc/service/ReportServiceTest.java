package com.flc.service;

import com.flc.FlcApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {

    @Test
    void reportsIncludeAllTimetableWeekendsThroughEight() {
        FlcApplicationContext ctx = FlcApplicationContext.createWithSampleData();
        ReportService reports = ctx.getReportService();
        assertEquals(8, reports.getReportWeekendEndInclusive());
        String r1 = reports.buildReportLessonAttendanceAndRatings();
        assertTrue(r1.contains("W8"), "Report 1 should list weekend 8 lessons, not stop at W4");
        String r2 = reports.buildReportHighestIncomeExercise();
        assertTrue(r2.contains("weekends 1") && r2.contains("8)"), "Report 2 header should span all weekends");
    }
}
