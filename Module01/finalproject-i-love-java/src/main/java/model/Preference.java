package model;

/**
 * Represents the selected health goal for the current session.
 */
public class Preference {

    private final String id;
    private final HealthGoal healthGoal;

    public Preference(String id, HealthGoal healthGoal) {
        this.id = id;
        this.healthGoal = healthGoal;
    }

    public String getId() {
        return id;
    }

    public HealthGoal getHealthGoal() {
        return healthGoal;
    }
}
