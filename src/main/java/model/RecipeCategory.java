package model;

public class RecipeCategory {
    // PRD: Recipe taxonomy only (Breakfast/Quick Meals...). Never merge with FoodCategory.
    private final String id;
    // Recipe card grouping label used on Recommendation page.
    private final String name;
    // Decorative category icon key for recipe UI tags.
    private final String icon;

    // Constructs an immutable recipe category loaded from recipes JSON.
    public RecipeCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    // Stable identifier used by deterministic recipe category filters.
    public String getId() {
        return id;
    }

    // Display name used by Recommendation category filters.
    public String getName() {
        return name;
    }

    // Returns recipe category icon token (visual metadata only).
    public String getIcon() {
        return icon;
    }
}
