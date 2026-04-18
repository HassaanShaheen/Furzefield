package com.flc.service;

import com.flc.model.Lesson;
import com.flc.model.Review;
import com.flc.model.enums.ExerciseType;
import com.flc.repository.BookingRepository;
import com.flc.repository.LessonRepository;
import com.flc.repository.ReviewRepository;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reports cover every weekend present in the timetable (all loaded lessons), so bookings
 * and ratings on any week (e.g. W8) appear together with W1–W4.
 */
public class ReportService {

    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public ReportService(
            LessonRepository lessonRepository,
            BookingRepository bookingRepository,
            ReviewRepository reviewRepository) {
        this.lessonRepository = lessonRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    /** Highest {@link Lesson#getWeekendNumber()} in the repository, or 1 if empty. */
    public int getReportWeekendEndInclusive() {
        return lessonRepository.findAll().stream()
                .mapToInt(Lesson::getWeekendNumber)
                .max()
                .orElse(1);
    }

    public String buildReportLessonAttendanceAndRatings() {
        int weekendEnd = getReportWeekendEndInclusive();
        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(l -> l.getWeekendNumber() >= 1 && l.getWeekendNumber() <= weekendEnd)
                .sorted(Comparator.comparingInt(Lesson::getWeekendNumber)
                        .thenComparing(l -> l.getDay().ordinal())
                        .thenComparing(l -> l.getTimeSlot().ordinal()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORT 1: Lesson attendance & average rating (weekends 1–")
                .append(weekendEnd).append(") ===\n\n");
        sb.append(String.format("%-6s %-10s %-10s %-18s %6s %8s %12s%n",
                "Week", "Day", "Slot", "Exercise", "Booked", "Reviews", "Avg rating"));
        sb.append("-".repeat(88)).append('\n');

        for (Lesson lesson : lessons) {
            long booked = bookingRepository.countByLesson(lesson);
            List<Review> revs = reviewRepository.findByLesson(lesson);
            double avg = revs.stream().mapToInt(Review::getRating).average().orElse(0.0);
            String avgStr = revs.isEmpty() ? "n/a" : String.format("%.2f", avg);
            sb.append(String.format(
                    "W%-5d %-10s %-10s %-18s %6d %8d %12s%n",
                    lesson.getWeekendNumber(),
                    lesson.getDay().name(),
                    lesson.getTimeSlot().name(),
                    lesson.getExerciseType().getDisplayName(),
                    booked,
                    revs.size(),
                    avgStr));
        }
        return sb.toString();
    }

    public String buildReportHighestIncomeExercise() {
        Map<ExerciseType, Double> incomeByType = new EnumMap<>(ExerciseType.class);
        for (ExerciseType t : ExerciseType.values()) {
            incomeByType.put(t, 0.0);
        }

        int weekendEnd = getReportWeekendEndInclusive();
        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(l -> l.getWeekendNumber() >= 1 && l.getWeekendNumber() <= weekendEnd)
                .toList();

        for (Lesson lesson : lessons) {
            long bookings = bookingRepository.countByLesson(lesson);
            double income = lesson.getPrice() * bookings;
            incomeByType.merge(lesson.getExerciseType(), income, Double::sum);
        }

        double max = incomeByType.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORT 2: Income by exercise type (weekends 1–")
                .append(weekendEnd).append(") ===\n\n");
        sb.append(String.format("%-18s %12s%n", "Exercise", "Total £"));
        sb.append("-".repeat(32)).append('\n');
        for (ExerciseType t : ExerciseType.values()) {
            sb.append(String.format("%-18s %12.2f%n", t.getDisplayName(), incomeByType.get(t)));
        }
        sb.append('\n');
        List<ExerciseType> winners = incomeByType.entrySet().stream()
                .filter(e -> Math.abs(e.getValue() - max) < 0.005)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(Enum::name))
                .toList();
        if (winners.isEmpty()) {
            sb.append("Highest income exercise: (none)\n");
        } else {
            sb.append("Highest income exercise");
            if (winners.size() > 1) {
                sb.append(" (tie): ");
            } else {
                sb.append(": ");
            }
            sb.append(winners.stream().map(ExerciseType::getDisplayName).collect(Collectors.joining(", ")));
            sb.append(String.format(" — £%.2f total%n", max));
        }
        return sb.toString();
    }
}
