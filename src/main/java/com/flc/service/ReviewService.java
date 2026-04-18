package com.flc.service;

import com.flc.model.Lesson;
import com.flc.model.Member;
import com.flc.model.Review;
import com.flc.repository.BookingRepository;
import com.flc.repository.ReviewRepository;

import java.util.Optional;

public class ReviewService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    public Optional<String> addReview(Member member, Lesson lesson, int rating, String comment) {
        boolean attended = bookingRepository.findByMember(member).stream()
                .anyMatch(b -> b.getLesson().getLessonId().equals(lesson.getLessonId()));
        if (!attended) {
            return Optional.of("You may only review lessons you have attended (have a booking for).");
        }
        if (reviewRepository.findByMemberAndLesson(member, lesson).isPresent()) {
            return Optional.of("You have already submitted a review for this lesson.");
        }
        if (rating < 1 || rating > 5) {
            return Optional.of("Rating must be between 1 and 5.");
        }
        String text = comment == null ? "" : comment.trim();
        reviewRepository.add(new Review(member, lesson, rating, text));
        return Optional.empty();
    }
}
