package com.flc;

import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;
import com.flc.model.enums.TimeSlot;

/**
 * Loads tutor baseline data: exercise types (via lessons), 10 demo members with accounts,
 * and 48 lessons (8 weekends). Bookings and reviews start empty — users create them after login.
 */
public final class SampleDataLoader {

    private SampleDataLoader() {}

    public static void load(FlcApplicationContext ctx) {
        seedMembersAndAuth(ctx);
        seedLessons(ctx);
    }

    private static void seedMembersAndAuth(FlcApplicationContext ctx) {
        String[][] rows = {
                {"m01", "Alice Ahmed"},
                {"m02", "Ben Brown"},
                {"m03", "Chloe Chen"},
                {"m04", "David Diaz"},
                {"m05", "Eva Evans"},
                {"m06", "Farah Khan"},
                {"m07", "George Green"},
                {"m08", "Hannah Hughes"},
                {"m09", "Ivan Ivanov"},
                {"m10", "Julia Jones"}
        };
        for (String[] row : rows) {
            Member m = new Member(row[0], row[1]);
            ctx.getMemberRepository().save(m);
            ctx.getAuthService().register(row[0], row[1], FlcApplicationContext.DEMO_PASSWORD, FlcApplicationContext.DEMO_PASSWORD);
        }
    }

    private static void seedLessons(FlcApplicationContext ctx) {
        ExerciseType[] types = ExerciseType.values();
        int i = 0;
        for (int weekend = 1; weekend <= 8; weekend++) {
            for (Day day : Day.values()) {
                for (TimeSlot slot : TimeSlot.values()) {
                    ExerciseType type = types[i % types.length];
                    double price = type.getPrice();
                    String id = "W" + weekend + "-" + day.name() + "-" + slot.name();
                    ctx.getLessonRepository().save(new Lesson(id, weekend, day, slot, type, price));
                    i++;
                }
            }
        }
    }
}
