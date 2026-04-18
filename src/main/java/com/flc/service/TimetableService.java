package com.flc.service;

import com.flc.model.Lesson;
import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;
import com.flc.repository.LessonRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableService {

    private final LessonRepository lessonRepository;

    public TimetableService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> allLessonsSorted() {
        return lessonRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Lesson::getWeekendNumber)
                        .thenComparing(l -> l.getDay().ordinal())
                        .thenComparing(l -> l.getTimeSlot().ordinal()))
                .collect(Collectors.toList());
    }

    public List<Lesson> filterByDay(Day day) {
        return lessonRepository.findByDay(day).stream()
                .sorted(Comparator.comparingInt(Lesson::getWeekendNumber)
                        .thenComparing(l -> l.getTimeSlot().ordinal()))
                .collect(Collectors.toList());
    }

    public List<Lesson> filterByExerciseType(ExerciseType type) {
        return lessonRepository.findByExerciseType(type).stream()
                .sorted(Comparator.comparingInt(Lesson::getWeekendNumber)
                        .thenComparing(l -> l.getDay().ordinal())
                        .thenComparing(l -> l.getTimeSlot().ordinal()))
                .collect(Collectors.toList());
    }
}
