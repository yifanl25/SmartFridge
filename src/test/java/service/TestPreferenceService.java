package service;

import model.HealthGoal;
import model.Preference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestPreferenceService {
    private PreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
    }

    @Test void testSavePreferenceStoresGoal() { assertEquals(HealthGoal.FAT_LOSS, preferenceService.savePreference(HealthGoal.FAT_LOSS).getHealthGoal()); }
    @Test void testGetPreferenceReturnsSavedGoal() { preferenceService.savePreference(HealthGoal.MUSCLE_BUILDING); assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceService.getPreference().getHealthGoal()); }
    @Test void testClearPreferenceRemovesCurrentPreference() { preferenceService.savePreference(HealthGoal.BLOOD_SUGAR_CARE); preferenceService.clearPreference(); assertNull(preferenceService.getPreference()); }
}
