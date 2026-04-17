import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserPreference {
    private UserPreference pref;

    @BeforeEach
    void setUp() {
        pref = new UserPreference();
    }

    /**
     * Test that a newly created UserPreference has no goals selected.
     */
    @Test
    void testInitialGoalListIsEmpty() {
        assertTrue(pref.getGoals().isEmpty());
    }

    /**
     * Test that selecting GOAL_MUSCLE_BUILDING stores it in the goals list.
     */
    @Test
    void testSelectGoalMuscleBuilding() {
        pref.selectGoal(UserPreference.GOAL_MUSCLE_BUILDING);
        assertEquals(List.of(UserPreference.GOAL_MUSCLE_BUILDING), pref.getGoals());
    }

    /**
     * Test that selecting GOAL_FAT_LOSS stores it in the goals list.
     */
    @Test
    void testSelectGoalFatLoss() {
        pref.selectGoal(UserPreference.GOAL_FAT_LOSS);
        assertEquals(List.of(UserPreference.GOAL_FAT_LOSS), pref.getGoals());
    }

    /**
     * Test that selecting GOAL_BLOOD_SUGAR_CARE stores it in the goals list.
     */
    @Test
    void testSelectGoalBloodSugarCare() {
        pref.selectGoal(UserPreference.GOAL_BLOOD_SUGAR_CARE);
        assertEquals(List.of(UserPreference.GOAL_BLOOD_SUGAR_CARE), pref.getGoals());
    }

    /**
     * Test that selecting a new goal replaces the previously selected one.
     * Only the latest goal should remain in the list.
     */
    @Test
    void testSelectGoalReplacesOldGoal() {
        pref.selectGoal(UserPreference.GOAL_MUSCLE_BUILDING);
        pref.selectGoal(UserPreference.GOAL_FAT_LOSS);
        assertEquals(1, pref.getGoals().size());
        assertEquals(UserPreference.GOAL_FAT_LOSS, pref.getGoals().get(0));
    }

    /**
     * Test that the goals list never exceeds one entry,
     * even after multiple selections.
     */
    @Test
    void testSelectGoalListAlwaysHasOneEntry() {
        pref.selectGoal(UserPreference.GOAL_MUSCLE_BUILDING);
        pref.selectGoal(UserPreference.GOAL_BLOOD_SUGAR_CARE);
        pref.selectGoal(UserPreference.GOAL_FAT_LOSS);
        assertEquals(1, pref.getGoals().size());
    }

    /**
     * Test that hasGoalSelected() returns true after a goal is selected.
     */
    @Test
    void testHasGoalSelectedAfterSelect() {
        pref.selectGoal(UserPreference.GOAL_FAT_LOSS);
        assertTrue(pref.hasGoalSelected());
    }

    /**
     * Test that hasGoalSelected() remains true after switching goals.
     */
    @Test
    void testHasGoalSelectedAfterSwitch() {
        pref.selectGoal(UserPreference.GOAL_MUSCLE_BUILDING);
        pref.selectGoal(UserPreference.GOAL_BLOOD_SUGAR_CARE);
        assertTrue(pref.hasGoalSelected());
    }

    /**
     * Test that setGoals() correctly updates the goals list directly.
     */
    @Test
    void testSetGoalsDirectly() {
        pref.setGoals(List.of(UserPreference.GOAL_FAT_LOSS));
        assertEquals(List.of(UserPreference.GOAL_FAT_LOSS), pref.getGoals());
    }
}
