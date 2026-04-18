package com.flc.model;

public class Booking {
    private final Member member;
    private final Lesson lesson;

    public Booking(Member member, Lesson lesson) {
        this.member = member;
        this.lesson = lesson;
    }

    public Member getMember() {
        return member;
    }

    public Lesson getLesson() {
        return lesson;
    }
}