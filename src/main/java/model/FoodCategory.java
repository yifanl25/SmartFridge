package model;

public class FoodCategory {
    // PRD: Ingredient taxonomy only (Dairy/Produce/Pantry...). Do not use for recipe filtering.
    private final String id;
    // Display name shown on Inventory/Grocery ingredient filters.
    private final String name;
    // Decorative category icon key used by UI.
    private final String icon;

    // Constructs an immutable ingredient category from static catalog data.
    public FoodCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    // Stable identifier from catalog JSON; used for deterministic category mapping.
    public String getId() {
        return id;
    }

    // Human-readable ingredient category label.
    public String getName() {
        return name;
    }

    // Returns icon token only; icon behavior itself stays decorative in MVP.
    public String getIcon() {
        return icon;
    }
}
