package model;

/**
 * User health objective selected for the session; drives recipe tag alignment in scoring.
 * <p>
 * 用户在本会话选择的健康目标；用于推荐打分中与菜谱 {@code healthTags} 的对齐。
 */
public enum HealthGoal {
    /**
     * Prefer HIGH_PROTEIN recipes, then BALANCED as fallback.
     * <p>
     * 优先高蛋白标签菜谱，其次平衡型作为回落。
     */
    MUSCLE_BUILDING,
    /**
     * Prefer LOW_CALORIE recipes, then BALANCED as fallback.
     * <p>
     * 优先低卡标签菜谱，其次平衡型作为回落。
     */
    FAT_LOSS,
    /**
     * Prefer BLOOD_SUGAR_FRIENDLY recipes, then BALANCED as fallback.
     * <p>
     * 优先控糖友好标签菜谱，其次平衡型作为回落。
     */
    BLOOD_SUGAR_CARE
}
