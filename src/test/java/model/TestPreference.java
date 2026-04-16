package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：用户偏好快照（对应 {@link Preference}，会话级、无持久化）。
 */
public class TestPreference {
    /**
     * 测试功能：不可变偏好对象持有 id 与 {@link HealthGoal}。
     * 验证点：getter 返回值正确。
     * <p>
     * 对应源码 / Maps to: {@link Preference#Preference(String, HealthGoal)}，{@link Preference#getId()}，{@link Preference#getHealthGoal()}
     */
    @Test
    void testConstructorAndGetters() {
        Preference preference = new Preference("p1", HealthGoal.FAT_LOSS);
        assertEquals("p1", preference.getId());
        assertEquals(HealthGoal.FAT_LOSS, preference.getHealthGoal());
    }
}
