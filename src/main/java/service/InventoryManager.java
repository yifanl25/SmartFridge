package service;

import model.FoodCategory;
import model.FoodItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Legacy helper for early demos; prefer {@link InventoryService} for PRD add-from-catalog flow.
 * <p>
 * 早期演示用的遗留辅助类；符合 PRD 的「从目录添加」请使用 {@link InventoryService}。
 */
public class InventoryManager {
    // 这个类<strong>没人用</strong>，主程序走的是 InventoryService + 目录 / Nobody uses this file; real app uses InventoryService + catalog.
    // 组长决定删不删 / Team lead: delete or keep for experiments only.
    // INSERT YOUR CODE HERE

    /** Legacy in-memory list (not defensive). / 遗留内存列表（非防御性 API）。 */
    private final List<FoodItem> inventory = new ArrayList<>();

    /**
     * Adds a manually constructed item (bypasses catalog suggestions).
     * <p>
     * 添加手工构造的条目（绕过目录建议流程）。
     */
    public void addFood(String name, String expiryDateStr, String category) {
        FoodCategory foodCategory = new FoodCategory(category, category, "icon");
        FoodItem newItem = new FoodItem(
                String.valueOf(System.currentTimeMillis()),
                name,
                foodCategory,
                1,
                "pcs",
                java.time.LocalDate.now().toString(),
                expiryDateStr
        );
        inventory.add(newItem);
    }

    /**
     * Sorts by expiry date ascending (static utility on a list).
     * <p>
     * 按过期日升序排序（对给定列表的静态工具）。
     */
    public static List<FoodItem> sortItemsByUrgency(List<FoodItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    /**
     * Filters legacy internal list by ingredient category name.
     * <p>
     * 按食材分类名筛选遗留内部列表。
     */
    public List<FoodItem> filterByCategory(String category) {
        return inventory.stream()
                .filter(item -> item.getCategory().getName().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Returns the backing list (legacy API; callers can mutate).
     * <p>
     * 返回 backing 列表（遗留 API；调用方可变）。
     */
    public List<FoodItem> getInventory() {
        return inventory;
    }
}
