package service;

import model.HealthGoal;
import model.Preference;

import java.util.UUID;

/**
 * Stores one preference in memory for the current session.
 */
public class PreferenceService implements IPreferenceService {

    private Preference currentPreference;

    public PreferenceService() {
    }

    /**
     * Saves the selected health goal for the current session.
     */
    @Override
    public Preference savePreference(HealthGoal goal) {
        currentPreference = new Preference(UUID.randomUUID().toString(), goal);
        return currentPreference;
    }

    /**
     * Returns the current preference, or null if none is set.
     */
    @Override
    public Preference getPreference() {
        return currentPreference;
    }

    /**
     * Clears the current preference.
     */
    @Override
    public void clearPreference() {
        currentPreference = null;
    }
}
