package ui;

import controller.InventoryController;
import model.FoodItem;

import java.util.List;

/**
 * Inventory screen demo: prints cards and shows sort/filter examples.
 */
public class InventoryPage {
    private final InventoryController inventoryController;

    public InventoryPage(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    public void render() {
        System.out.println("[Inventory Page]");
        List<FoodItem> items = inventoryController.getVisibleItems();
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("All items:");
        for (FoodItem item : items) {
            System.out.println("- " + item.getName() + " | " + item.getCategory().getName()
                    + " | qty " + item.getQuantity() + " " + item.getUnit()
                    + " | expiry " + item.getExpiryDate());
        }
        System.out.println("Earliest expiry preview:");
        inventoryController.sortByExpiry().stream().limit(3)
                .forEach(item -> System.out.println("  * " + item.getName() + " -> " + item.getExpiryDate()));
    }
}
