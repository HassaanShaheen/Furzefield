package com.flc.ui.fx;

import com.flc.FlcApplicationContext;
import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.service.BookingService;

import java.util.List;

/**
 * Which lessons appear in book / change-booking lists: full slots and unusable targets are omitted.
 */
public final class LessonListFilters {

    private LessonListFilters() {}

    public static boolean lessonHasFreeSpace(FlcApplicationContext ctx, Lesson lesson) {
        return ctx.getBookingRepository().countByLesson(lesson) < BookingService.MAX_MEMBERS_PER_LESSON;
    }

    public static boolean memberHasBookingOnLesson(FlcApplicationContext ctx, Member member, Lesson lesson) {
        return ctx.getBookingRepository().findByMember(member).stream()
                .anyMatch(b -> b.getLesson().getLessonId().equals(lesson.getLessonId()));
    }

    /** Lessons the member can still book (not full, not already on that lesson). */
    public static List<Lesson> forBookLessonTable(FlcApplicationContext ctx, Member member) {
        return ctx.getTimetableService().allLessonsSorted().stream()
                .filter(l -> lessonHasFreeSpace(ctx, l))
                .filter(l -> !memberHasBookingOnLesson(ctx, member, l))
                .toList();
    }

    /**
     * Lessons the member may move the given booking to: not full, not the same lesson, and not a lesson
     * they already hold under another booking. When {@code selectedBooking} is null, only the
     * “not full” rule applies so the table is populated before a booking row is chosen.
     */
    public static List<Lesson> forChangeBookingTargetTable(FlcApplicationContext ctx, Member member, Booking selectedBooking) {
        var stream = ctx.getTimetableService().allLessonsSorted().stream()
                .filter(l -> lessonHasFreeSpace(ctx, l));
        if (selectedBooking == null) {
            return stream.toList();
        }
        String bookingId = selectedBooking.getBookingId();
        String currentLessonId = selectedBooking.getLesson().getLessonId();
        return stream
                .filter(l -> !l.getLessonId().equals(currentLessonId))
                .filter(l -> ctx.getBookingRepository().findByMember(member).stream()
                        .noneMatch(b -> !b.getBookingId().equals(bookingId)
                                && b.getLesson().getLessonId().equals(l.getLessonId())))
                .toList();
    }
}
