import java.util.ArrayList;
import java.util.List;

/**
 * Stores the user's health goals selected on the Preference Setup page.
 * <p>
 * Users must select one goal before they can continue.
 * All other preference fields are optional and can be added later.
 */
public class UserPreference {

    /**
     * Health goal constant for muscle building.
     */
    public static final String GOAL_MUSCLE_BUILDING = "muscle_building";
    /**
     * Health goal constant for fat loss.
     */
    public static final String GOAL_FAT_LOSS = "fat_loss";
    /**
     * Health goal constant for blood sugar care.
     */
    public static final String GOAL_BLOOD_SUGAR_CARE = "blood_sugar_care";
    /**
     * The list of health goals the user has selected.
     */
    private List<String> goals;

    /**
     * Constructs a new UserPreference with no goals selected.
     */
    public UserPreference() {
        this.goals = new ArrayList<>();
    }

    /**
     * Toggles a health goal on or off.
     * <p>
     * If the goal is already selected, it will be removed.
     * If it is not selected, it will be added.
     * Call this when the user clicks one of the three goal cards.
     *
     * @param goal one of the goal constants
     */
    public void toggleGoal(String goal) {
        if (goals.contains(goal)) {
            goals.remove(goal);
        } else {
            goals.add(goal);
        }
    }

    /**
     * Returns true if the user has selected at least one health goal.
     * Use this to control whether the Continue button is enabled.
     *
     * @return true if at least one goal is selected, false otherwise
     */
    public boolean hasGoalSelected() {
        return !goals.isEmpty();
    }

    /**
     * Returns the list of selected health goals.
     *
     * @return list of goal strings
     */
    public List<String> getGoals() {
        return goals;
    }

    /**
     * Sets the list of health goals directly.
     *
     * @param goals list of goal strings to set
     */
    public void setGoals(List<String> goals) {
        this.goals = goals;
    }
}