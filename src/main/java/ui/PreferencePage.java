package ui;

import controller.PreferenceController;
import model.HealthGoal;

/**
 * Stub Preference screen: user picks one {@link HealthGoal} for the session.
 * <p>
 * 偏好页占位：用户为会话选择一个 {@link HealthGoal}。
 */
public class PreferencePage {
    private final PreferenceController preferenceController;

    /**
     * @param preferenceController preference controller / 偏好控制器
     */
    public PreferencePage(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    /** Placeholder render. / 占位渲染。 */
    public void render() {
        // 这里应该让用户点一个目标，然后调用下面的 submitGoal / Here: let user pick a goal, then call submitGoal.
        // 检查规则写在 PreferenceController 里，别在这里抄一遍 / Validation stays in controller layer.
        // INSERT YOUR CODE HERE
        System.out.println("Preference Page");
    }

    /**
     * Saves selected goal into session preference.
     * <p>
     * 将所选目标写入会话偏好。
     */
    public void submitGoal(HealthGoal goal) {
        preferenceController.savePreference(goal);
    }
}
