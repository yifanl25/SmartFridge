package model;

/**
 * Recipe-side taxonomy (e.g. Quick Meals); not interchangeable with {@link FoodCategory}.
 * <p>
 */
public class RecipeCategory {
    /** Stable id from recipes JSON. */
    private final String id;
    /** Display label on recommendation cards and filters. */
    private final String name;
    /** Decorative icon key for recipe UI. */
    private final String icon;

    /**
     * Creates an immutable recipe category from JSON.
     * <p>
     */
    public RecipeCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /** Returns category id. */
    public String getId() {
        return id;
    }

    /** Returns display name. */
    public String getName() {
        return name;
    }

    /** Returns icon token. */
    public String getIcon() {
        return icon;
    }
}
