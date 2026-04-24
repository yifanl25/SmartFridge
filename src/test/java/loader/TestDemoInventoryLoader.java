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
 * TDD：Demo inventory data loading tests.（Maps to: {@link DemoInventoryLoader}、resource file {@code data.json}）。
 */
public class TestDemoInventoryLoader {
    /**
     * Test：Reads data {@code /data.json} from classpath and maps into the list of {@link FoodItem}.
     * Verification：number of items and the first item's expiry date match the JSON data.
     * Also verifies integration with {@code food_catalog.json} parsing.
     * <p>
     * Maps to production:
     * {@link DemoInventoryLoader#loadFoodItemsOptional(String, IFoodCatalog)}，
     * helps {@link JsonFoodCatalogLoader#loadFromFileSafe(String)} for catalog loading.
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
