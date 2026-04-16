package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：食材目录条目（对应 {@link FoodCatalogEntry}，源自 {@code food_catalog.json}）。
 */
public class TestFoodCatalogEntry {
    /**
     * 测试功能：目录行绑定规范名、默认保质天数、食材分类。
     * 验证点：字段与构造一致。
     * <p>
     * 对应源码 / Maps to: {@link FoodCatalogEntry#FoodCatalogEntry(String, int, FoodCategory)}，
     * {@link FoodCatalogEntry#getFoodName()}，{@link FoodCatalogEntry#getDefaultExpiryDays()}，{@link FoodCatalogEntry#getCategory()}
     */
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalogEntry entry = new FoodCatalogEntry("Milk", 7, category);
        assertEquals("Milk", entry.getFoodName());
        assertEquals(7, entry.getDefaultExpiryDays());
        assertEquals(category, entry.getCategory());
    }
}
