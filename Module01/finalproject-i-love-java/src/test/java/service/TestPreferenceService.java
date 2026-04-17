package service;

import model.HealthGoal;
import model.Preference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * TDD：会话内偏好存储（对应 {@link PreferenceService}，无持久化）。
 */
public class TestPreferenceService {
    private PreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
    }

    /**
     * 测试功能：保存 {@link HealthGoal} 供推荐评分使用。
     * 验证点：返回对象携带同一枚举值。
     * <p>
     * 对应源码 / Maps to: {@link PreferenceService#savePreference(HealthGoal)}
     */
    @Test
    void testSavePreferenceStoresGoal() {
        assertEquals(HealthGoal.FAT_LOSS, preferenceService.savePreference(HealthGoal.FAT_LOSS).getHealthGoal());
    }

    /**
     * 测试功能：读取当前会话最近一次保存的偏好。
     * 验证点：与最后一次写入一致。
     * <p>
     * 对应源码 / Maps to: {@link PreferenceService#getPreference()}
     */
    @Test
    void testGetPreferenceReturnsSavedGoal() {
        preferenceService.savePreference(HealthGoal.MUSCLE_BUILDING);
        assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceService.getPreference().getHealthGoal());
    }

    /**
     * 测试功能：结账/会话重置时清除偏好。
     * 验证点：{@code getPreference()} 为 {@code null}。
     * <p>
     * 对应源码 / Maps to: {@link PreferenceService#clearPreference()}, {@link PreferenceService#getPreference()}
     */
    @Test
    void testClearPreferenceRemovesCurrentPreference() {
        preferenceService.savePreference(HealthGoal.BLOOD_SUGAR_CARE);
        preferenceService.clearPreference();
        assertNull(preferenceService.getPreference());
    }
}
