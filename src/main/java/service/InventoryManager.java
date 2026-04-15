package service;

import model.FoodCategory;
import model.FoodItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    // Legacy helper inventory store kept for backward compatibility/demo only.
    // Main PRD flow should rely on InventoryService.
    private final List<FoodItem> inventory = new ArrayList<>();

    // Legacy add method: manually creates item.
    // PRD-compliant Add Item should go through suggestion-based InventoryService.addItem.
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

    // Legacy static sorter by expiry date ascending.
    public static List<FoodItem> sortItemsByUrgency(List<FoodItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    // Legacy filter by ingredient category name.
    public List<FoodItem> filterByCategory(String category) {
        return inventory.stream()
                .filter(item -> item.getCategory().getName().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // Returns underlying list (non-defensive legacy API).
    public List<FoodItem> getInventory() {
        return inventory;
    }
}
