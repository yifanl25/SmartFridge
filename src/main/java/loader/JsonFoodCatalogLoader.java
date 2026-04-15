package loader;

import model.FoodCatalogEntry;
import model.FoodCategory;

import java.util.ArrayList;
import java.util.List;

public final class JsonFoodCatalogLoader {
    // Utility loader; stateless and not instantiable.
    private JsonFoodCatalogLoader() {
    }

    // Load static food catalog data.
    // PRD contract:
    // - only static JSON source (food_catalog.json)
    // - no runtime persistence/write-back
    // - output is source-of-truth for AddItem suggestions and defaultExpiryDays
    // Current body is scaffold data and should be replaced by real JSON parsing.
    public static List<FoodCatalogEntry> loadFromFile(String path) {
        List<FoodCatalogEntry> entries = new ArrayList<>();
        FoodCategory defaultCategory = new FoodCategory("default", "Default", "food");
        entries.add(new FoodCatalogEntry("Milk", 7, defaultCategory));
        entries.add(new FoodCatalogEntry("Egg", 14, defaultCategory));
        return entries;
    }
}
