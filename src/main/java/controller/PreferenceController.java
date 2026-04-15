package controller;

import model.HealthGoal;
import model.Preference;
import service.IPreferenceService;

public class PreferenceController {
    // Controller boundary for preference setup page actions.
    private final IPreferenceService preferenceService;

    // Inject preference service abstraction for session-state management.
    public PreferenceController(IPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // Save selected health goal for the current demo loop.
    public Preference savePreference(HealthGoal goal) { return preferenceService.savePreference(goal); }

    // Read currently selected health goal for downstream recommendation input.
    public Preference getPreference() { return preferenceService.getPreference(); }
}
