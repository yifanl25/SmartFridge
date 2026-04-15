package model;

public class Preference {
    // Session-scoped preference id; reset after checkout per PRD loop rule.
    private final String id;
    // Selected health objective used for recommendation alignment scoring.
    private final HealthGoal healthGoal;

    // Immutable preference snapshot for current session only.
    public Preference(String id, HealthGoal healthGoal) {
        this.id = id;
        this.healthGoal = healthGoal;
    }

    // Returns preference identifier for replacement/clear operations.
    public String getId() {
        return id;
    }

    // Returns selected health goal used in preferenceAlignmentScore.
    public HealthGoal getHealthGoal() {
        return healthGoal;
    }
}
