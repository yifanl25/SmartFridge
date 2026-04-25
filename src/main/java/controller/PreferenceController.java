package controller;

import model.HealthGoal;
import model.Preference;
import service.IPreferenceService;

/**
 * Internal coordination layer for session preference operations.
 * <p>
 * This class is not part of the HTTP boundary. Spring annotations stay in
 * {@code api.web.PreferenceApiController}; this layer remains a thin facade over
 * {@link IPreferenceService}.
 */
public class PreferenceController {
    /** Session preference service.  */
    private final IPreferenceService preferenceService;

    /**
     * @param preferenceService injected preference implementation
     */
    public PreferenceController(IPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Persists selected {@link HealthGoal} for the session.
     * <p>
     */
    public Preference savePreference(HealthGoal goal) {
        return preferenceService.savePreference(goal);
    }

    /**
     * Returns current preference or {@code null} if unset.
     * <p>
     */
    public Preference getPreference() {
        return preferenceService.getPreference();
    }
}
