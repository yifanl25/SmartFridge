package api.dto;

/**
 * Request body for saving the selected health goal.
 */
public class PreferenceRequest {

    private String healthGoal;

    public String getHealthGoal() {
        return healthGoal;
    }

    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }
}
