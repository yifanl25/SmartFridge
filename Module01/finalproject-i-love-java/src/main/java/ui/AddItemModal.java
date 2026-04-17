package ui;

import controller.InventoryController;

/**
 * Add Item modal demo.
 */
public class AddItemModal {
    private final InventoryController inventoryController;

    public AddItemModal(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    public void render() {
        System.out.println("[Add Item Modal]");
        System.out.println("Pick one suggested food name, then call submitFoodName(...).");
        System.out.println("Example: submitFoodName(\"Eggs\") or use console command: add eggs");
    }

    public void submitFoodName(String foodName) {
        inventoryController.addItem(foodName);
    }
}
