package com.flc.model.enums;

public enum ExerciseType {
    YOGA,
    ZUMBA,
    AQUACISE,
    BOX_FIT,
    BODY_BLITZ;

    public String getDisplayName() {
        return switch (this) {
            case YOGA -> "Yoga";
            case ZUMBA -> "Zumba";
            case AQUACISE -> "Aquacise";
            case BOX_FIT -> "Box Fit";
            case BODY_BLITZ -> "Body Blitz";
        };
    }

    /** Same price for every lesson of this exercise type (tutor requirement). */
    public double getPrice() {
        return switch (this) {
            case YOGA -> 10.0;
            case ZUMBA -> 12.0;
            case AQUACISE -> 14.0;
            case BOX_FIT -> 11.0;
            case BODY_BLITZ -> 13.0;
        };
    }
}