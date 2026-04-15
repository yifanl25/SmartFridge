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
     * Selects a health goal, replacing any previously selected goal.
     * Only one goal can be selected at a time.
     * Call this when the user clicks one of the three goal cards.
     *
     * @param goal one of the goal constants
     */
    public void selectGoal(String goal) {
        goals.clear();
        goals.add(goal);
    }

    /**
     * Returns true if the user has selected ONLY one health goal.
     *
     * Use this to control whether the Continue button is enabled.
     *
     * @return true if ONLY one goal is selected, false otherwise
     */
    public boolean hasGoalSelected() {
        return goals.size() == 1;
    }

    /**
     * Returns the list of selected health goals.
     * Will always contain at most one entry.
     *
     * @return list of selected goal strings
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