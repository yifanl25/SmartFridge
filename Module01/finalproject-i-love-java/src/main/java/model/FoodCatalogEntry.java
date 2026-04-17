package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one entry from the food catalog.
 */
public class FoodCatalogEntry {

    private final String id;
    private final String foodName;
    private final List<String> aliases;
    private final int defaultExpiryDays;
    private final FoodCategory category;

    public FoodCatalogEntry(
            String id,
            String foodName,
            List<String> aliases,
            int defaultExpiryDays,
            FoodCategory category) {
        this.id = id;
        this.foodName = foodName;
        this.aliases = new ArrayList<>(aliases);
        this.defaultExpiryDays = defaultExpiryDays;
        this.category = category;
    }

    public FoodCatalogEntry(String foodName, int defaultExpiryDays, FoodCategory category) {
        this(null, foodName, new ArrayList<>(), defaultExpiryDays, category);
    }

    public String getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    public int getDefaultExpiryDays() {
        return defaultExpiryDays;
    }

    public FoodCategory getCategory() {
        return category;
    }
}
