package loader;

import model.FoodItem;
import model.FoodCatalogEntry;
import service.FoodCatalog;
import service.IFoodCatalog;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：演示用库存种子数据加载（对应 {@link DemoInventoryLoader}、资源文件 {@code data.json}）。
 */
public class TestDemoInventoryLoader {
    /**
     * 测试功能：从 classpath 读取 {@code /data.json} 并映射为 {@link FoodItem} 列表。
     * 验证点：条数与首条过期日与 JSON 一致；与 {@code food_catalog.json} 联动解析。
     * <p>
     * 对应源码 / Maps to production:
     * {@link DemoInventoryLoader#loadFoodItemsOptional(String, IFoodCatalog)}，
     * 辅助 {@link JsonFoodCatalogLoader#loadFromFileSafe(String)} 加载目录。
     */
    @Test
    void testLoadsDemoInventoryFromClasspath() throws IOException {
        List<FoodCatalogEntry> catalogEntries = JsonFoodCatalogLoader.loadFromFileSafe("/food_catalog.json");
        FoodCatalog foodCatalog = new FoodCatalog(catalogEntries);
        List<FoodItem> items = DemoInventoryLoader.loadFoodItemsOptional("/data.json", foodCatalog);
        assertEquals(20, items.size());
        assertEquals("2026-04-18", items.get(0).getExpiryDate());
    }
}
