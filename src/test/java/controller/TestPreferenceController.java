package controller;

import model.HealthGoal;
import service.IPreferenceService;
import service.PreferenceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：PreferenceController delegations tests.（Maps to: {@link PreferenceController} → {@link IPreferenceService}）。
 */
public class TestPreferenceController {
    private PreferenceController preferenceController;
    private IPreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
        preferenceController = new PreferenceController(preferenceService);
    }

    /**
     * Test: SavePreference and the returned matches  {@link HealthGoal} input value.
     * <p>
     * Maps to: {@link PreferenceController#savePreference(HealthGoal)} → {@link PreferenceService#savePreference(HealthGoal)}
     */
    @Test
    void testSavePreferenceDelegatesToPreferenceService() {
        assertEquals(HealthGoal.FAT_LOSS, preferenceController.savePreference(HealthGoal.FAT_LOSS).getHealthGoal());
    }

    /**
     * Test: Reads GetPreference and matches the mostely saved in {@code savePreference}.
     * <p>
     * Maps to: {@link PreferenceController#getPreference()} → {@link PreferenceService#getPreference()}
     */
    @Test
    void testGetPreferenceDelegatesToPreferenceService() {
        preferenceController.savePreference(HealthGoal.MUSCLE_BUILDING);
        assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceController.getPreference().getHealthGoal());
    }
}
