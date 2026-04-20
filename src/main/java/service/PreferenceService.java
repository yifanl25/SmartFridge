package service;

import model.HealthGoal;
import model.Preference;

import java.util.UUID;

/**
 * Holds at most one {@link Preference} in memory for the current session.
 * <p>
 */
public class PreferenceService implements IPreferenceService {
    /** Current preference or null if unset. */
    private Preference currentPreference;

    /** Creates an empty preference service. */
    public PreferenceService() {
    }

    /** {@inheritDoc} */
    @Override
    public Preference savePreference(HealthGoal goal) {
        currentPreference = new Preference(UUID.randomUUID().toString(), goal);
        return currentPreference;
    }

    /** {@inheritDoc} */
    @Override
    public Preference getPreference() {
        return currentPreference;
    }

    /** {@inheritDoc} */
    @Override
    public void clearPreference() {
        currentPreference = null;
    }
}
