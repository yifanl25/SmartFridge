package model;

import java.util.ArrayList;
import java.util.List;

/**
 * One row from {@code food_catalog.json}: canonical name, aliases, default shelf life, and ingredient category.
 * <p>
 * {@code food_catalog.json} 中的一行：规范名、别名、默认保质期天数及食材分类。
 */
public class FoodCatalogEntry {
    /** Optional stable id from JSON. / JSON 中的可选稳定 id。 */
    private final String id;
    /** Canonical display name. / 规范显示名称。 */
    private final String foodName;
    /** Alternate searchable strings. / 可搜索的别名列表。 */
    private final List<String> aliases;
    /** Days from add-date to default expiry. / 从添加日起算的默认保质天数。 */
    private final int defaultExpiryDays;
    /** Ingredient category for this food. / 该食材所属分类。 */
    private final FoodCategory category;

    /**
     * Full constructor including id and aliases.
     * <p>
     * 包含 id 与别名的完整构造。
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
     * <p>
     * 当 JSON 省略 id 与别名时的简便构造。
     */
    public FoodCatalogEntry(String foodName, int defaultExpiryDays, FoodCategory category) {
        this(null, foodName, List.of(), defaultExpiryDays, category);
    }

    /** Returns entry id or null if omitted. / 返回条目 id；若省略则为 null。 */
    public String getId() {
        return id;
    }

    /** Returns canonical food name. / 返回规范食材名。 */
    public String getFoodName() {
        return foodName;
    }

    /** Returns a defensive copy of aliases. / 返回别名的防御性拷贝。 */
    public List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    /** Returns configured default expiry offset in days. / 返回配置的默认保质天数。 */
    public int getDefaultExpiryDays() {
        return defaultExpiryDays;
    }

    /** Returns ingredient category. / 返回食材分类。 */
    public FoodCategory getCategory() {
        return category;
    }
}
