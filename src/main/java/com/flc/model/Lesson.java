package com.flc.model;

import com.flc.model.enums.Day;
import com.flc.model.enums.ExerciseType;
import com.flc.model.enums.TimeSlot;

public class Lesson {
    private final String lessonId;
    private final int weekendNumber;
    private final Day day;
    private final TimeSlot timeSlot;
    private final ExerciseType exerciseType;
    private final double price;

    public Lesson(String lessonId, int weekendNumber, Day day, TimeSlot timeSlot, ExerciseType exerciseType, double price) {
        this.lessonId = lessonId;
        this.weekendNumber = weekendNumber;
        this.day = day;
        this.timeSlot = timeSlot;
        this.exerciseType = exerciseType;
        this.price = price;
    }

    public String getLessonId() {
        return lessonId;
    }

    public int getWeekendNumber() {
        return weekendNumber;
    }

    public Day getDay() {
        return day;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public double getPrice() {
        return price;
    }
}