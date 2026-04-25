package service;

import model.FoodCategory;
import model.FoodItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Legacy helper for early demos; prefer {@link InventoryService} for PRD add-from-catalog flow.
 */
public class InventoryManager {
    // <strong>Nobody uses this file; real app uses InventoryService + catalog.
    // Team lead decides to delete or keep for experiments only.
    // INSERT YOUR CODE HERE

    /** Legacy in-memory list (not defensive).  */
    private final List<FoodItem> inventory = new ArrayList<>();

    /**
     * Adds a manually constructed item (bypasses catalog suggestions).
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
     */
    public static List<FoodItem> sortItemsByUrgency(List<FoodItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    /**
     * Filters legacy internal list by ingredient category name.
     */
    public List<FoodItem> filterByCategory(String category) {
        return inventory.stream()
                .filter(item -> item.getCategory().getName().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Returns the backing list (legacy API; callers can mutate).
     */
    public List<FoodItem> getInventory() {
        return inventory;
    }
}
