package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：菜谱分类（对应 {@link RecipeCategory}，与食材 {@link FoodCategory} 分离）。
 */
public class TestRecipeCategory {
    /**
     * 测试功能：菜谱分组标签的 id、名称、图标。
     * 验证点：不可变字段经 getter 读出。
     * <p>
     * 对应源码 / Maps to: {@link RecipeCategory#RecipeCategory(String, String, String)}，
     * {@link RecipeCategory#getId()}，{@link RecipeCategory#getName()}，{@link RecipeCategory#getIcon()}
     */
    @Test
    void testConstructorAndGetters() {
        RecipeCategory category = new RecipeCategory("r1", "Quick", "bolt");
        assertEquals("r1", category.getId());
        assertEquals("Quick", category.getName());
        assertEquals("bolt", category.getIcon());
    }
}
