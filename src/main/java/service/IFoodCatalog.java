package service;

import model.FoodCatalogEntry;

import java.util.List;
import java.util.Optional;

/**
 * Read-only food catalog: suggestions, membership, default expiry, alias resolution.
 * <p>
 * 只读食材目录：联想建议、是否包含、默认保质期、别名解析。
 */
public interface IFoodCatalog {

    /**
     * Prefix search for Add Item suggestion list.
     * <p>
     * 前缀搜索，用于「添加食材」联想列表。
     */
    List<FoodCatalogEntry> searchSuggestions(String prefix);

    /**
     * Whether the catalog recognizes this food name (including aliases).
     * <p>
     * 目录是否识别该食材名（含别名）。
     */
    boolean containsFood(String foodName);

    /**
     * Default shelf-life offset in days for inventory expiry when adding this food.
     * <p>
     * 添加该食材时用于计算默认过期日的保质天数。
     */
    int getDefaultExpiryDays(String foodName);

    /**
     * Resolves a catalog row by canonical name or alias (trim/lowercase normalization in implementation).
     * <p>
     * 按规范名或别名解析目录行（实现中做 trim/小写等规范化）。
     */
    Optional<FoodCatalogEntry> resolveEntry(String foodName);

    /**
     * Canonical display string for cross-matching recipe ingredients vs inventory names.
     * <p>
     * 用于菜谱配料与库存名称交叉匹配的规范显示字符串。
     */
    String canonicalFoodName(String raw);
}
