package model;

import java.util.ArrayList;
import java.util.List;

/**
 * One row from {@code food_catalog.json}: canonical name, aliases, default shelf life, and ingredient category.
 */
public class FoodCatalogEntry {
    /** Optional stable id from JSON.  */
    private final String id;
    /** Canonical display name.  */
    private final String foodName;
    /** Alternate searchable strings.  */
    private final List<String> aliases;
    /** Days from add-date to default expiry.  */
    private final int defaultExpiryDays;
    /** Ingredient category for this food.  */
    private final FoodCategory category;

    /**
     * Full constructor including id and aliases.
     */
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

    /**
     * Convenience constructor when JSON omits id and aliases.
     */
    public FoodCatalogEntry(String foodName, int defaultExpiryDays, FoodCategory category) {
        this(null, foodName, List.of(), defaultExpiryDays, category);
    }

    /** Returns entry id or null if omitted.  */
    public String getId() {
        return id;
    }

    /** Returns canonical food name.  */
    public String getFoodName() {
        return foodName;
    }

    /** Returns a defensive copy of aliases.  */
    public List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    /** Returns configured default expiry offset in days.  */
    public int getDefaultExpiryDays() {
        return defaultExpiryDays;
    }

    /** Returns ingredient category.  */
    public FoodCategory getCategory() {
        return category;
    }
}
