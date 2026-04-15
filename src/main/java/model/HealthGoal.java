package model;

public enum HealthGoal {
    // Prefer recipes tagged HIGH_PROTEIN first, then BALANCED as fallback alignment.
    MUSCLE_BUILDING,
    // Prefer recipes tagged LOW_CALORIE first, then BALANCED as fallback alignment.
    FAT_LOSS,
    // Prefer recipes tagged BLOOD_SUGAR_FRIENDLY first, then BALANCED fallback.
    BLOOD_SUGAR_CARE
}
