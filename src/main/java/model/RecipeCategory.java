package model;

/**
 * Represents a recipe category (e.g. Breakfast, Dinner, Quick Meals).
 * <p>
 * This is the recipe-side taxonomy and is not interchangeable with {@link FoodCategory},
 * which classifies food ingredients. Recipe categories are used to group and filter
 * recipes on the recommendation page.
 * </p>
 */
public class RecipeCategory {
    /**
     * Stable identifier from the recipes JSON file.
     */
    private final String id;
    /**
     * Display label on recommendation cards and filters.
     */
    private final String name;
    /**
     * Decorative icon key for recipe UI.
     */
    private final String icon;

    /**
     * Constructs an immutable recipe category loaded from JSON.
     *
     * @param id   stable category identifier
     * @param name display label shown in the UI
     * @param icon icon key for the recipe UI
     */
    public RecipeCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /**
     * Returns the stable category identifier.
     *
     * @return category ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name shown on recommendation cards and filters.
     *
     * @return category display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the icon key used for decorative display in the recipe UI.
     *
     * @return icon key string
     */
    public String getIcon() {
        return icon;
    }
}
