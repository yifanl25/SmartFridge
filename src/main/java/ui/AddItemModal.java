package ui;

import controller.InventoryController;

public class AddItemModal {
    // Controller gateway for Add Item submission.
    private final InventoryController inventoryController;

    // Wire add-item modal to inventory controller.
    public AddItemModal(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    // Render Add Item modal container.
    // PRD interaction notes:
    // - modal stays open after successful ADD
    // - close only via overlay click or X button
    public void render() {
        System.out.println("Add Item Modal");
    }

    // Submit selected suggestion text to create one FoodItem.
    // PRD: submission must use suggestion selection path (no free-form custom item outside suggestions).
    public void submitFoodName(String foodName) {
        inventoryController.addItem(foodName);
    }
}
