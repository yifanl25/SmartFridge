package controller;

import model.HealthGoal;
import model.Preference;
import service.IPreferenceService;

/**
 * Controller for the preference setup flow.
 */
public class PreferenceController {

    private final IPreferenceService preferenceService;

    public PreferenceController(IPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Saves the selected health goal for the current session.
     */
    public Preference savePreference(HealthGoal goal) {
        return preferenceService.savePreference(goal);
    }

    /**
     * Returns the current preference, or null if none is set.
     */
    public Preference getPreference() {
        return preferenceService.getPreference();
    }

    /**
     * Clears the current session preference.
     */
    public void clearPreference() {
        preferenceService.clearPreference();
    }
}
