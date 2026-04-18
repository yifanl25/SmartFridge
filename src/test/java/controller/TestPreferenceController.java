package controller;

import model.HealthGoal;
import service.IPreferenceService;
import service.PreferenceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：偏好/健康目标控制器（对应 {@link PreferenceController} → {@link IPreferenceService}）。
 */
public class TestPreferenceController {
    private PreferenceController preferenceController;
    private IPreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceService();
        preferenceController = new PreferenceController(preferenceService);
    }

    /**
     * 测试功能：保存用户健康目标。
     * 验证点：返回值中的 {@link HealthGoal} 与入参一致。
     * <p>
     * 对应源码 / Maps to: {@link PreferenceController#savePreference(HealthGoal)} → {@link PreferenceService#savePreference(HealthGoal)}
     */
    @Test
    void testSavePreferenceDelegatesToPreferenceService() {
        assertEquals(HealthGoal.FAT_LOSS, preferenceController.savePreference(HealthGoal.FAT_LOSS).getHealthGoal());
    }

    /**
     * 测试功能：读取当前会话已保存的偏好。
     * 验证点：与最近一次 {@code savePreference} 一致。
     * <p>
     * 对应源码 / Maps to: {@link PreferenceController#getPreference()} → {@link PreferenceService#getPreference()}
     */
    @Test
    void testGetPreferenceDelegatesToPreferenceService() {
        preferenceController.savePreference(HealthGoal.MUSCLE_BUILDING);
        assertEquals(HealthGoal.MUSCLE_BUILDING, preferenceController.getPreference().getHealthGoal());
    }
}
