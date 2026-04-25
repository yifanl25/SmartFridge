package model;

/**
 * Immutable ingredient taxonomy for inventory and grocery lists (e.g. Dairy, Produce).
 */
public class FoodCategory {
    /** Stable id from catalog JSON.  */
    private final String id;
    /** Human-readable label for filters and UI.  */
    private final String name;
    /** Decorative icon key for UI.  */
    private final String icon;

    /**
     * Creates an ingredient category row from static catalog data.
     *
     * @param id   stable category identifier
     * @param name display name
     * @param icon icon token
     */
    public FoodCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /**
     * Returns the category id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the icon key (visual metadata only in MVP).
     */
    public String getIcon() {
        return icon;
    }
}
