package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHealthGoal {
    @Test
    void testEnumValues() {
        assertEquals(HealthGoal.MUSCLE_BUILDING, HealthGoal.valueOf("MUSCLE_BUILDING"));
        assertEquals(HealthGoal.FAT_LOSS, HealthGoal.valueOf("FAT_LOSS"));
        assertEquals(HealthGoal.BLOOD_SUGAR_CARE, HealthGoal.valueOf("BLOOD_SUGAR_CARE"));
    }
}
