package api.dto;

/**
 * Request body for PUT /api/preference.
 * <p>
 * The {@code healthGoal} field must be one of the following valid strings
 * (case-insensitive): {@code MUSCLE_BUILDING}, {@code FAT_LOSS}, {@code BLOOD_SUGAR_CARE}.
 * </p>
 */
public class PreferenceRequest {

    /**
     * The health goal string selected by the user on the preference page.
     */
    private String healthGoal;

    /**
     * Returns the health goal string from the request body.
     *
     * @return the health goal string
     */
    public String getHealthGoal() {
        return healthGoal;
    }

    /**
     * Sets the health goal string.
     *
     * @param healthGoal one of: MUSCLE_BUILDING, FAT_LOSS, BLOOD_SUGAR_CARE
     */
    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }
}
