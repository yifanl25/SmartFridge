package service;

import model.HealthGoal;
import model.Preference;

public interface IPreferenceService {
    // Save selected health goal as current session preference.
    Preference savePreference(HealthGoal goal);

    // Get current preference snapshot for recommendation input.
    Preference getPreference();

    // Clear session-only preference after checkout loop end.
    void clearPreference();
}
