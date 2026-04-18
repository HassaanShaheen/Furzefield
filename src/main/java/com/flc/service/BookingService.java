package com.flc.service;

import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.repository.BookingRepository;

import java.util.List;
import java.util.Optional;

public class BookingService {

    public static final int MAX_MEMBERS_PER_LESSON = 4;

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Time conflict: member cannot hold two bookings for the same scheduled slot
     * (same weekend number, calendar day, and time band). Different weekends are independent.
     */
    public boolean hasTimeConflict(Member member, Lesson candidate, Booking exclude) {
        for (Booking b : bookingRepository.findByMember(member)) {
            if (exclude != null && b.getBookingId().equals(exclude.getBookingId())) {
                continue;
            }
            Lesson existing = b.getLesson();
            if (sameScheduledSlot(existing, candidate)) {
                return true;
            }
        }
        return false;
    }

    private static boolean sameScheduledSlot(Lesson a, Lesson b) {
        return a.getWeekendNumber() == b.getWeekendNumber()
                && a.getDay() == b.getDay()
                && a.getTimeSlot() == b.getTimeSlot();
    }

    public Optional<String> book(Member member, Lesson lesson) {
        if (bookingRepository.countByLesson(lesson) >= MAX_MEMBERS_PER_LESSON) {
            return Optional.of("That lesson is full (maximum " + MAX_MEMBERS_PER_LESSON + " members).");
        }
        for (Booking b : bookingRepository.findByMember(member)) {
            if (b.getLesson().getLessonId().equals(lesson.getLessonId())) {
                return Optional.of("You are already booked on this lesson.");
            }
        }
        if (hasTimeConflict(member, lesson, null)) {
            return Optional.of("You already have another lesson at the same weekend, day and time slot.");
        }
        bookingRepository.add(new Booking(member, lesson));
        return Optional.empty();
    }

    public Optional<String> changeBooking(String bookingId, Member member, Lesson newLesson) {
        Optional<Booking> existingOpt = bookingRepository.findById(bookingId);
        if (existingOpt.isEmpty()) {
            return Optional.of("Booking not found.");
        }
        Booking existing = existingOpt.get();
        if (!existing.getMember().getMemberId().equals(member.getMemberId())) {
            return Optional.of("This booking does not belong to you.");
        }
        if (existing.getLesson().getLessonId().equals(newLesson.getLessonId())) {
            return Optional.of("Select a different lesson to change to.");
        }
        if (bookingRepository.countByLesson(newLesson) >= MAX_MEMBERS_PER_LESSON) {
            return Optional.of("The new lesson is full.");
        }
        if (hasTimeConflict(member, newLesson, existing)) {
            return Optional.of("The new lesson conflicts with another of your bookings (same weekend, day and time slot).");
        }
        for (Booking b : bookingRepository.findByMember(member)) {
            if (b.getLesson().getLessonId().equals(newLesson.getLessonId())) {
                return Optional.of("You are already booked on the selected lesson.");
            }
        }
        bookingRepository.removeById(bookingId);
        bookingRepository.add(new Booking(member, newLesson));
        return Optional.empty();
    }

    public List<Booking> listBookingsForMember(Member member) {
        return bookingRepository.findByMember(member);
    }
}
