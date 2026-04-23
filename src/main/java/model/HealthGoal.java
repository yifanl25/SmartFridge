package model;

/**
 * Represents the health goal a user selects on the Preference Setup page.
 * <p>
 * Used by the recommendation service to align recipe health tags with the
 * user's goal and award preference alignment bonus points during scoring.
 * Each goal maps to a preferred {@link Recipe.HealthTag} with {@code BALANCED}
 * as a fallback if no exact tag match is found.
 * </p>
 */
public enum HealthGoal {
    /**
     * Prefer HIGH_PROTEIN recipes, then BALANCED as fallback.
     * <p>
     */
    MUSCLE_BUILDING,
    /**
     * Prefer LOW_CALORIE recipes, then BALANCED as fallback.
     * <p>
     */
    FAT_LOSS,
    /**
     * Prefer BLOOD_SUGAR_FRIENDLY recipes, then BALANCED as fallback.
     * <p>
     */
    BLOOD_SUGAR_CARE
}
