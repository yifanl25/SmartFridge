package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：食材/配料分类（对应 {@link FoodCategory}，仅用于库存与购物，不用于菜谱筛选）。
 */
public class TestFoodCategory {
    /**
     * 测试功能：目录中的食材分类 id、名称、图标。
     * 验证点：与静态目录数据字段一致。
     * <p>
     * 对应源码 / Maps to: {@link FoodCategory#FoodCategory(String, String, String)}，
     * {@link FoodCategory#getId()}，{@link FoodCategory#getName()}，{@link FoodCategory#getIcon()}
     */
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        assertEquals("c1", category.getId());
        assertEquals("Dairy", category.getName());
        assertEquals("milk", category.getIcon());
    }
}
