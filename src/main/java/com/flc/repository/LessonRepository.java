package com.flc.repository;

import com.flc.model.Lesson;
import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LessonRepository {

    private final Map<String, Lesson> byId = new LinkedHashMap<>();

    public void save(Lesson lesson) {
        byId.put(lesson.getLessonId(), lesson);
    }

    public Optional<Lesson> findById(String lessonId) {
        return Optional.ofNullable(byId.get(lessonId));
    }

    public List<Lesson> findAll() {
        return new ArrayList<>(byId.values());
    }

    public List<Lesson> findByDay(Day day) {
        return byId.values().stream()
                .filter(l -> l.getDay() == day)
                .collect(Collectors.toList());
    }

    public List<Lesson> findByExerciseType(ExerciseType type) {
        return byId.values().stream()
                .filter(l -> l.getExerciseType() == type)
                .collect(Collectors.toList());
    }
}
