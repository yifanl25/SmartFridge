package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：Health goal enum consistency test.（Maps to: {@link HealthGoal}）。
 */
public class TestHealthGoal {
    /**
     * Test：enum constants and {@code valueOf} parsing (used in session preferences, CLI commands, etc.).
     * Verification: all PRD-defined health goals can be correctly resolved by name.
     * <p>
     * Maps to: {@link HealthGoal#valueOf(String)}（and {@link HealthGoal#MUSCLE_BUILDING}、{@link HealthGoal#FAT_LOSS}、{@link HealthGoal#BLOOD_SUGAR_CARE}）
     */
    @Test
    void testEnumValues() {
        assertEquals(HealthGoal.MUSCLE_BUILDING, HealthGoal.valueOf("MUSCLE_BUILDING"));
        assertEquals(HealthGoal.FAT_LOSS, HealthGoal.valueOf("FAT_LOSS"));
        assertEquals(HealthGoal.BLOOD_SUGAR_CARE, HealthGoal.valueOf("BLOOD_SUGAR_CARE"));
    }
}
