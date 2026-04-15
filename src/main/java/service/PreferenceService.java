package service;

import model.HealthGoal;
import model.Preference;

import java.util.UUID;

public class PreferenceService implements IPreferenceService {
    // Session-only current preference; null means not selected yet.
    private Preference currentPreference;

    // Stateless constructor; service holds runtime state only.
    public PreferenceService() {
    }

    @Override
    // Persist selected goal in memory for current loop only.
    public Preference savePreference(HealthGoal goal) {
        currentPreference = new Preference(UUID.randomUUID().toString(), goal);
        return currentPreference;
    }

    @Override
    // Return current preference or null if user has not configured one.
    public Preference getPreference() {
        return currentPreference;
    }

    @Override
    // Reset preference at checkout so next app start behaves first-time.
    public void clearPreference() {
        currentPreference = null;
    }
}
