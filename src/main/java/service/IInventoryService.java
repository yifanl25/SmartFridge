package service;

import model.FoodItem;

import java.util.List;

/**
 * Session-scoped inventory operations (add, query, filter, sort, clear).
 * <p>
 * 会话级库存操作：查询、添加、筛选、排序、清空。
 */
public interface IInventoryService {

    /**
     * Returns all items currently in memory for this session.
     * <p>
     * 返回本会话内存中的全部库存项。
     */
    List<FoodItem> getAllItems();

    /**
     * Adds one item resolved from catalog by {@code foodName} (suggestion flow).
     * <p>
     * 根据 {@code foodName} 经目录解析添加一条库存（建议列表流程）。
     */
    FoodItem addItem(String foodName);

    /**
     * Appends pre-built items (e.g. demo seed from {@code data.json}).
     * <p>
     * 批量追加已构造好的 {@link FoodItem}（例如来自 {@code data.json} 的演示种子）。
     */
    void addAllItems(List<FoodItem> toAdd);

    /**
     * Filters by ingredient {@link model.FoodCategory} name (not recipe category).
     * <p>
     * 按食材 {@link model.FoodCategory} 名称筛选（非菜谱分类）。
     */
    List<FoodItem> filterByCategory(String categoryName);

    /**
     * Sorts by expiry date (earlier first for urgency browsing).
     * <p>
     * 按过期日排序（越早越靠前，便于临期浏览）。
     */
    List<FoodItem> sortByExpiry();

    /**
     * Sorts by created date (newest first in implementation).
     * <p>
     * 按创建/入库日排序（实现中为新到旧）。
     */
    List<FoodItem> sortByCreatedTime();

    /**
     * Clears all inventory rows (checkout / session reset).
     * <p>
     * 清空全部库存行（结账/会话重置）。
     */
    void clearInventory();
}
