package controller;

import model.HealthGoal;
import model.Preference;
import service.IPreferenceService;

/**
 * MVC controller for the Preference setup screen; delegates to {@link IPreferenceService}.
 * <p>
 * 偏好设置页控制器；委托 {@link IPreferenceService}。
 */
public class PreferenceController {
    /** Session preference service. / 会话偏好服务。 */
    private final IPreferenceService preferenceService;

    /**
     * @param preferenceService injected preference implementation / 注入的偏好实现
     */
    public PreferenceController(IPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Persists selected {@link HealthGoal} for the session.
     * <p>
     * 保存本会话所选 {@link HealthGoal}。
     */
    public Preference savePreference(HealthGoal goal) {
        return preferenceService.savePreference(goal);
    }

    /**
     * Returns current preference or {@code null} if unset.
     * <p>
     * 返回当前偏好；未设置时为 {@code null}。
     */
    public Preference getPreference() {
        return preferenceService.getPreference();
    }
}
