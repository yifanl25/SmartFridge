package model;

/**
 * Represents one ingredient category used by inventory and grocery items.
 */
public class FoodCategory {

    private final String id;
    private final String name;
    private final String icon;

    public FoodCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
