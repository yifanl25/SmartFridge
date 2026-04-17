package ui;

import controller.PreferenceController;
import model.HealthGoal;
import model.Preference;

/**
 * Stub Preference page: user picks one session-wide {@link HealthGoal}.
 * <p>
 * 偏好页占位：用户为会话选择一个 {@link HealthGoal}。
 */
public class PreferencePage {
    private final PreferenceController preferenceController;

    public PreferencePage(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    /**
     * Demo render: if no goal exists, auto-pick one so the recommendation flow can continue.
     */
    public void render() {
        System.out.println("[Preference Page]");
        Preference current = preferenceController.getPreference();
        if (current == null) {
            current = preferenceController.savePreference(HealthGoal.MUSCLE_BUILDING);
            System.out.println("Auto-selected demo goal: " + current.getHealthGoal());
        } else {
            System.out.println("Current goal: " + current.getHealthGoal());
        }
    }

    public void submitGoal(HealthGoal goal) {
        preferenceController.savePreference(goal);
    }
}
