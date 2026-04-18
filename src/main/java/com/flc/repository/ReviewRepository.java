package com.flc.repository;

import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReviewRepository {

    private final List<Review> reviews = new ArrayList<>();

    public void add(Review review) {
        reviews.add(review);
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviews);
    }

    public List<Review> findByLesson(Lesson lesson) {
        return reviews.stream()
                .filter(r -> r.getLesson().getLessonId().equals(lesson.getLessonId()))
                .collect(Collectors.toList());
    }

    public Optional<Review> findByMemberAndLesson(Member member, Lesson lesson) {
        return reviews.stream()
                .filter(r -> r.getMember().getMemberId().equals(member.getMemberId())
                        && r.getLesson().getLessonId().equals(lesson.getLessonId()))
                .findFirst();
    }

    public boolean hasReview(Member member, Lesson lesson) {
        return findByMemberAndLesson(member, lesson).isPresent();
    }

    /** All reviews by this member, oldest first (stable for the session). */
    public List<Review> findByMember(Member member) {
        return reviews.stream()
                .filter(r -> r.getMember().getMemberId().equals(member.getMemberId()))
                .collect(Collectors.toList());
    }
}
