package model;

/**
 * Immutable snapshot of the user's selected health goal for the current demo session (cleared on checkout).
 * <p>
 */
public class Preference {
    /** Session-scoped preference id. */
    private final String id;
    /** Selected goal used by recommendation scoring. */
    private final HealthGoal healthGoal;

    /**
     * Creates a preference value object.
     * <p>
     */
    public Preference(String id, HealthGoal healthGoal) {
        this.id = id;
        this.healthGoal = healthGoal;
    }

    /** Returns preference id. */
    public String getId() {
        return id;
    }

    /** Returns selected health goal. */
    public HealthGoal getHealthGoal() {
        return healthGoal;
    }
}
