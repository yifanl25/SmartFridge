package ui;

import controller.InventoryController;

public class InventoryPage {
    // Controller gateway for inventory read/sort/filter/add actions.
    private final InventoryController inventoryController;

    // Wire inventory page to controller.
    public InventoryPage(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    // Render inventory cards with current session item count.
    // PRD notes:
    // - supports New/Urgent states
    // - supports FoodCategory filter and created/expiry sorting
    // - normal card click is non-functional in MVP
    public void render() {
        System.out.println("Inventory items: " + inventoryController.getVisibleItems().size());
    }
}
