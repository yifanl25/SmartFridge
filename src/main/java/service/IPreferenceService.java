package service;

import model.HealthGoal;
import model.Preference;

/**
 * Session-only user health goal for recommendation alignment (no persistence).
 * <p>
 */
public interface IPreferenceService {

    /**
     * Saves the selected {@link HealthGoal} for this session.
     * <p>
     */
    Preference savePreference(HealthGoal goal);

    /**
     * Returns the current preference snapshot, or null if none saved.
     * <p>
     */
    Preference getPreference();

    /**
     * Clears preference at checkout / full session reset.
     * <p>
     */
    void clearPreference();
}
