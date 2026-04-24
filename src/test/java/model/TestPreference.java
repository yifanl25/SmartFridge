package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：User preference.（Maps to: {@link Preference}）
 */
public class TestPreference {
    /**
     * Test：immutable preference object stores id and {@link HealthGoal}.
     * Verification：getter returns correctly.
     * <p>
     * Maps to: {@link Preference#Preference(String, HealthGoal)}，{@link Preference#getId()}，{@link Preference#getHealthGoal()}
     */
    @Test
    void testConstructorAndGetters() {
        Preference preference = new Preference("p1", HealthGoal.FAT_LOSS);
        assertEquals("p1", preference.getId());
        assertEquals(HealthGoal.FAT_LOSS, preference.getHealthGoal());
    }
}
