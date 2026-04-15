package model;

public class FoodCatalogEntry {
    // Canonical food name used by AddItem suggestion selection.
    private final String foodName;
    // PRD Add Item rule: expiryDate = createdAt + defaultExpiryDays.
    private final int defaultExpiryDays;
    // Ingredient category resolved from food_catalog.json.
    private final FoodCategory category;

    // Immutable catalog row; in MVP this should come from static JSON only.
    public FoodCatalogEntry(String foodName, int defaultExpiryDays, FoodCategory category) {
        this.foodName = foodName;
        this.defaultExpiryDays = defaultExpiryDays;
        this.category = category;
    }

    // Returns canonical suggestion text; custom free-form names are out of MVP scope.
    public String getFoodName() {
        return foodName;
    }

    // Returns configured shelf-life offset used when creating FoodItem.
    public int getDefaultExpiryDays() {
        return defaultExpiryDays;
    }

    // Maps a selected suggestion to its ingredient category.
    public FoodCategory getCategory() {
        return category;
    }
}
