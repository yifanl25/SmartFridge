package service;

import model.HealthGoal;
import model.Preference;

/**
 * Session-only user health goal for recommendation alignment (no persistence).
 * <p>
 * 会话内用户健康目标，用于推荐对齐打分；不做持久化。
 */
public interface IPreferenceService {

    /**
     * Saves the selected {@link HealthGoal} for this session.
     * <p>
     * 保存本会话所选 {@link HealthGoal}。
     */
    Preference savePreference(HealthGoal goal);

    /**
     * Returns the current preference snapshot, or null if none saved.
     * <p>
     * 返回当前偏好快照；若未保存则为 null。
     */
    Preference getPreference();

    /**
     * Clears preference at checkout / full session reset.
     * <p>
     * 在结账/完整会话重置时清除偏好。
     */
    void clearPreference();
}
