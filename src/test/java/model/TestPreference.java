package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPreference {
    @Test
    void testConstructorAndGetters() {
        Preference preference = new Preference("p1", HealthGoal.FAT_LOSS);
        assertEquals("p1", preference.getId());
        assertEquals(HealthGoal.FAT_LOSS, preference.getHealthGoal());
    }
}
