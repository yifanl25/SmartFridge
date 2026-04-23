package service;

import model.HealthGoal;
import model.Preference;

import java.util.UUID;

/**
 * In-memory implementation of {@link IPreferenceService}.
 * <p>
 * Holds at most one {@link Preference} per session. The preference is reset
 * when {@link #clearPreference()} is called or the application restarts.
 * </p>
 */
public class PreferenceService implements IPreferenceService {
    /**
     * Current preference or null if unset.
     */
    private Preference currentPreference;

    /**
     * Creates an empty preference service.
     */
    public PreferenceService() {
    }

    /**
     * Saves the user's selected health goal as the current preference.
     * <p>
     * Creates a new {@link Preference} with a generated ID and replaces
     * any previously saved preference.
     * </p>
     *
     * @param goal the health goal selected by the user
     * @return the newly created {@link Preference}
     */
    @Override
    public Preference savePreference(HealthGoal goal) {
        currentPreference = new Preference(UUID.randomUUID().toString(), goal);
        return currentPreference;
    }

    /**
     * Returns the current user preference.
     *
     * @return the current {@link Preference}, or {@code null} if none has been set
     */
    @Override
    public Preference getPreference() {
        return currentPreference;
    }

    /**
     * Clears the current preference, resetting it to {@code null}.
     * <p>
     * Call this when the user logs out or starts a new session.
     * </p>
     */
    @Override
    public void clearPreference() {
        currentPreference = null;
    }
}
