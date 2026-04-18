package model;

/**
 * Recipe-side taxonomy (e.g. Quick Meals); not interchangeable with {@link FoodCategory}.
 * <p>
 * 菜谱侧分类（如快手菜）；与食材分类 {@link FoodCategory} 不可混用。
 */
public class RecipeCategory {
    /** Stable id from recipes JSON. / 来自菜谱 JSON 的稳定 id。 */
    private final String id;
    /** Display label on recommendation cards and filters. / 推荐卡片与筛选上的显示标签。 */
    private final String name;
    /** Decorative icon key for recipe UI. / 菜谱界面装饰图标键。 */
    private final String icon;

    /**
     * Creates an immutable recipe category from JSON.
     * <p>
     * 根据 JSON 创建不可变菜谱分类。
     */
    public RecipeCategory(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /** Returns category id. / 返回分类 id。 */
    public String getId() {
        return id;
    }

    /** Returns display name. / 返回显示名称。 */
    public String getName() {
        return name;
    }

    /** Returns icon token. / 返回图标 token。 */
    public String getIcon() {
        return icon;
    }
}
