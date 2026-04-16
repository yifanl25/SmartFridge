package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：健康目标枚举与推荐标签对齐（对应 {@link HealthGoal}）。
 */
public class TestHealthGoal {
    /**
     * 测试功能：枚举常量与 {@code valueOf} 解析（会话偏好、CLI 命令等）。
     * 验证点：三个 PRD 目标均可按名称还原。
     * <p>
     * 对应源码 / Maps to: {@link HealthGoal#valueOf(String)}（及 {@link HealthGoal#MUSCLE_BUILDING}、{@link HealthGoal#FAT_LOSS}、{@link HealthGoal#BLOOD_SUGAR_CARE}）
     */
    @Test
    void testEnumValues() {
        assertEquals(HealthGoal.MUSCLE_BUILDING, HealthGoal.valueOf("MUSCLE_BUILDING"));
        assertEquals(HealthGoal.FAT_LOSS, HealthGoal.valueOf("FAT_LOSS"));
        assertEquals(HealthGoal.BLOOD_SUGAR_CARE, HealthGoal.valueOf("BLOOD_SUGAR_CARE"));
    }
}
