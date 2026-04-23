package model;

/**
 * Immutable value object representing the user's selected health goal for the current session.
 * <p>
 * Created by {@link service.PreferenceService} when the user confirms their selection
 * on the Preference Setup page, and cleared when the session ends.
 * Passed to the recommendation service to drive recipe scoring and tag alignment.
 * </p>
 */
public class Preference {
    /**
     * Session-scoped preference id.
     */
    private final String id;
    /**
     * Selected goal used by recommendation scoring.
     */
    private final HealthGoal healthGoal;

    /**
     * Constructs an immutable Preference with the given ID and health goal.
     *
     * @param id         unique identifier for this preference
     * @param healthGoal the health goal selected by the user
     */
    public Preference(String id, HealthGoal healthGoal) {
        this.id = id;
        this.healthGoal = healthGoal;
    }

    /**
     * Returns the unique identifier for this preference.
     *
     * @return preference ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user's selected health goal.
     * Used by the recommendation service to align recipes with the user's preference.
     *
     * @return the selected {@link HealthGoal}
     */
    public HealthGoal getHealthGoal() {
        return healthGoal;
    }
}
