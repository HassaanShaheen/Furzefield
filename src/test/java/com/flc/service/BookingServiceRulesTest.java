package com.flc.service;

import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;
import com.flc.model.enums.TimeSlot;
import com.flc.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingServiceRulesTest {

    private BookingRepository bookings;
    private BookingService service;
    private Lesson satMorningW1;
    private Lesson satMorningW2;
    private Lesson satAfternoonW1;
    private Member alice;

    @BeforeEach
    void setUp() {
        bookings = new BookingRepository();
        service = new BookingService(bookings);
        alice = new Member("alice", "Alice");
        satMorningW1 = new Lesson("W1-SAT-MORNING", 1, Day.SATURDAY, TimeSlot.MORNING, ExerciseType.YOGA, 10);
        satMorningW2 = new Lesson("W2-SAT-MORNING", 2, Day.SATURDAY, TimeSlot.MORNING, ExerciseType.ZUMBA, 12);
        satAfternoonW1 = new Lesson("W1-SAT-AFTERNOON", 1, Day.SATURDAY, TimeSlot.AFTERNOON, ExerciseType.YOGA, 10);
    }

    @Test
    void fifthMemberRejectedWhenLessonFull() {
        for (int i = 0; i < 4; i++) {
            Member m = new Member("m" + i, "M" + i);
            assertTrue(service.book(m, satMorningW1).isEmpty());
        }
        assertTrue(service.book(new Member("m5", "Five"), satMorningW1).isPresent());
    }

    @Test
    void sameDayAndSlotAllowedOnDifferentWeekends() {
        assertTrue(service.book(alice, satMorningW1).isEmpty());
        assertTrue(service.book(alice, satMorningW2).isEmpty());
    }

    @Test
    void sameWeekendDayAndSlotConflictWhenTwoLessonIdsShareSlot() {
        Lesson satMorningW1Alt = new Lesson("W1-SAT-MORNING-ALT", 1, Day.SATURDAY, TimeSlot.MORNING, ExerciseType.BODY_BLITZ, 11);
        assertTrue(service.book(alice, satMorningW1).isEmpty());
        assertTrue(service.book(alice, satMorningW1Alt).isPresent());
    }

    @Test
    void changeBookingAllowsSameDaySlotOnDifferentWeekend() {
        assertTrue(service.book(alice, satMorningW1).isEmpty());
        Booking b = bookings.findByMember(alice).get(0);
        assertTrue(service.changeBooking(b.getBookingId(), alice, satMorningW2).isEmpty());
        assertEquals(1, bookings.findByMember(alice).size());
        assertEquals(satMorningW2.getLessonId(), bookings.findByMember(alice).get(0).getLesson().getLessonId());
    }

    @Test
    void differentSlotSameDayAllowed() {
        assertTrue(service.book(alice, satMorningW1).isEmpty());
        assertTrue(service.book(alice, satAfternoonW1).isEmpty());
    }

    @Test
    void changeBookingRejectsWhenTargetFull() {
        for (int i = 0; i < 4; i++) {
            assertTrue(service.book(new Member("f" + i, "F" + i), satAfternoonW1).isEmpty());
        }
        assertTrue(service.book(alice, satMorningW1).isEmpty());
        Booking b = bookings.findByMember(alice).get(0);
        assertTrue(service.changeBooking(b.getBookingId(), alice, satAfternoonW1).isPresent());
    }
}
