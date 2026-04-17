package service;

import model.FoodCatalogEntry;
import model.FoodCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：静态食材目录查询与别名（对应 {@link FoodCatalog} / {@link IFoodCatalog}）。
 */
public class TestFoodCatalog {
    private FoodCatalog foodCatalog;

    /**
     * 构造与 {@code food_catalog.json} 片段一致的内存目录，供本类各用例复用。
     */
    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat),
                new FoodCatalogEntry("Cheese", 14, cat)));
    }

    /**
     * 测试功能：按前缀搜索建议（添加食材时的联想列表）。
     * 验证点：命中条数与预期一致。
     * <p>
     * 对应源码 / Maps to: {@link FoodCatalog#searchSuggestions(String)}
     */
    @Test
    void testSearchSuggestionsReturnsMatchingEntries() {
        assertEquals(1, foodCatalog.searchSuggestions("Mil").size());
    }

    /**
     * 测试功能：判断某名称是否在目录中（含别名解析）。
     * 验证点：已知食材返回 true。
     * <p>
     * 对应源码 / Maps to: {@link FoodCatalog#containsFood(String)}
     */
    @Test
    void testContainsFoodReturnsTrueWhenFoodExists() {
        assertTrue(foodCatalog.containsFood("Milk"));
    }

    /**
     * 测试功能：查询某食材默认保质天数（用于计算默认过期日）。
     * 验证点：与目录配置一致。
     * <p>
     * 对应源码 / Maps to: {@link FoodCatalog#getDefaultExpiryDays(String)}
     */
    @Test
    void testGetDefaultExpiryDaysReturnsConfiguredValue() {
        assertEquals(7, foodCatalog.getDefaultExpiryDays("Milk"));
    }
}
