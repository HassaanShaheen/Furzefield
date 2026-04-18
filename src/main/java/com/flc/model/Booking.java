package com.flc.model;

import java.util.UUID;

public class Booking {
    private final String bookingId;
    private final Member member;
    private final Lesson lesson;

    public Booking(Member member, Lesson lesson) {
        this(UUID.randomUUID().toString(), member, lesson);
    }

    public Booking(String bookingId, Member member, Lesson lesson) {
        this.bookingId = bookingId;
        this.member = member;
        this.lesson = lesson;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Member getMember() {
        return member;
    }

    public Lesson getLesson() {
        return lesson;
    }
}