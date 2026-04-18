package model;

/**
 * Immutable ingredient taxonomy for inventory and grocery lists (e.g. Dairy, Produce).
 * <p>
 * 不可变的「食材/配料」分类，用于库存与购物清单展示与筛选；不得用于菜谱筛选（菜谱分类请用 {@link RecipeCategory}）。
 */
public class FoodCategory {
    /** Stable id from catalog JSON. / 目录 JSON 中的稳定分类 id。 */
    private final String id;
    /** Human-readable label for filters and UI. / 人类可读名称，用于筛选与界面。 */
    private final String name;
    /** Decorative icon key for UI. / 界面装饰用图标键。 */
    private final String icon;

    /**
     * Creates an ingredient category row from static catalog data.
     * <p>
     * 根据静态目录数据构造一条食材分类。
     *
     * @param id   stable category identifier / 稳定分类标识
     * @param name display name / 显示名称
     * @param icon icon token / 图标 token
     */
    public FoodCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /**
     * Returns the category id.
     * <p>
     * 返回分类 id。
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name.
     * <p>
     * 返回显示名称。
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the icon key (visual metadata only in MVP).
     * <p>
     * 返回图标键（MVP 中仅作展示元数据）。
     */
    public String getIcon() {
        return icon;
    }
}
