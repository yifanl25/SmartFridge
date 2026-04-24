package service;

import model.HealthGoal;
import model.Preference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * TDD：Preference stores in session.(Maps to: {@link PreferenceService}，no persistence).
 */
public class TestPreferenceService {
    private PreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
    }

    /**
     * Test：Stores {@link HealthGoal} for recommendation scoring.
     * Verification: Returned object contains the same enum value.
     * <p>
     * Maps to: {@link PreferenceService#savePreference(HealthGoal)}
     */
    @Test
    void testSavePreferenceStoresGoal() {
        assertEquals(HealthGoal.FAT_LOSS, preferenceService.savePreference(HealthGoal.FAT_LOSS).getHealthGoal());
    }

    /**
     * Test: Retrieves the most recently saved preference in the session.
     * Verification: Matches the last saved value.
     * <p>
     * Maps to: {@link PreferenceService#getPreference()}
     */
    @Test
    void testGetPreferenceReturnsSavedGoal() {
        preferenceService.savePreference(HealthGoal.MUSCLE_BUILDING);
        assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceService.getPreference().getHealthGoal());
    }

    /**
     * Test: Clears preference on checkout or session reset.
     * Verification: {@code getPreference()} returns {@code null}.
     * <p>
     * Maps to: {@link PreferenceService#clearPreference()}, {@link PreferenceService#getPreference()}
     */
    @Test
    void testClearPreferenceRemovesCurrentPreference() {
        preferenceService.savePreference(HealthGoal.BLOOD_SUGAR_CARE);
        preferenceService.clearPreference();
        assertNull(preferenceService.getPreference());
    }
}
