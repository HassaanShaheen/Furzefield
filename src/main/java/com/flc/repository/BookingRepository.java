package com.flc.repository;

import com.flc.model.Booking;
import com.flc.model.Lesson;
import com.flc.model.Member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingRepository {

    private final List<Booking> bookings = new ArrayList<>();

    public void add(Booking booking) {
        bookings.add(booking);
    }

    public void removeById(String bookingId) {
        Iterator<Booking> it = bookings.iterator();
        while (it.hasNext()) {
            if (it.next().getBookingId().equals(bookingId)) {
                it.remove();
                return;
            }
        }
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }

    public List<Booking> findByMember(Member member) {
        return bookings.stream()
                .filter(b -> b.getMember().getMemberId().equals(member.getMemberId()))
                .collect(Collectors.toList());
    }

    public List<Booking> findByLesson(Lesson lesson) {
        return bookings.stream()
                .filter(b -> b.getLesson().getLessonId().equals(lesson.getLessonId()))
                .collect(Collectors.toList());
    }

    public long countByLesson(Lesson lesson) {
        return findByLesson(lesson).size();
    }

    public Optional<Booking> findById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst();
    }
}
