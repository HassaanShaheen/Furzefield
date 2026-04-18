package com.flc;

import com.flc.model.enums.ExerciseType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlcDataRequirementsTest {

    @Test
    void sampleDataMeetsTutorMinimums() {
        FlcApplicationContext ctx = FlcApplicationContext.createWithSampleData();
        assertEquals(48, ctx.getLessonRepository().findAll().size());
        assertEquals(10, ctx.getMemberRepository().findAll().size());
        assertEquals(0, ctx.getBookingRepository().findAll().size());
        assertEquals(0, ctx.getReviewRepository().findAll().size());
        assertTrue(ExerciseType.values().length >= 4);
    }
}
