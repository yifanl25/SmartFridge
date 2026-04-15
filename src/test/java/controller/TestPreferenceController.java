package controller;

import model.HealthGoal;
import model.Preference;
import service.IPreferenceService;
import service.PreferenceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPreferenceController {
    private PreferenceController preferenceController;
    private IPreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
        preferenceController = new PreferenceController(preferenceService);
    }

    @Test void testSavePreferenceDelegatesToPreferenceService() { assertEquals(HealthGoal.FAT_LOSS, preferenceController.savePreference(HealthGoal.FAT_LOSS).getHealthGoal()); }
    @Test void testGetPreferenceDelegatesToPreferenceService() { preferenceController.savePreference(HealthGoal.MUSCLE_BUILDING); assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceController.getPreference().getHealthGoal()); }
}
