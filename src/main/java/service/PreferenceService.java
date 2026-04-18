package service;

import model.HealthGoal;
import model.Preference;

import java.util.UUID;

/**
 * Holds at most one {@link Preference} in memory for the current session.
 * <p>
 * 在内存中至多保存一个 {@link Preference}，表示本会话用户偏好。
 */
public class PreferenceService implements IPreferenceService {
    /** Current preference or null if unset. / 当前偏好；未设置时为 null。 */
    private Preference currentPreference;

    /** Creates an empty preference service. / 创建无初始偏好的服务。 */
    public PreferenceService() {
    }

    /** {@inheritDoc} */
    @Override
    public Preference savePreference(HealthGoal goal) {
        currentPreference = new Preference(UUID.randomUUID().toString(), goal);
        return currentPreference;
    }

    /** {@inheritDoc} */
    @Override
    public Preference getPreference() {
        return currentPreference;
    }

    /** {@inheritDoc} */
    @Override
    public void clearPreference() {
        currentPreference = null;
    }
}
