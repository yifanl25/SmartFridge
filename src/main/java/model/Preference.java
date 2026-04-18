package model;

/**
 * Immutable snapshot of the user's selected health goal for the current demo session (cleared on checkout).
 * <p>
 * 当前演示会话中用户所选健康目标的不可变快照；结账后会话清空时一并清除。
 */
public class Preference {
    /** Session-scoped preference id. / 会话范围内的偏好 id。 */
    private final String id;
    /** Selected goal used by recommendation scoring. / 用于推荐打分的所选目标。 */
    private final HealthGoal healthGoal;

    /**
     * Creates a preference value object.
     * <p>
     * 创建偏好值对象。
     */
    public Preference(String id, HealthGoal healthGoal) {
        this.id = id;
        this.healthGoal = healthGoal;
    }

    /** Returns preference id. / 返回偏好 id。 */
    public String getId() {
        return id;
    }

    /** Returns selected health goal. / 返回所选健康目标。 */
    public HealthGoal getHealthGoal() {
        return healthGoal;
    }
}
