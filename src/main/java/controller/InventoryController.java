package controller;

import model.FoodItem;
import service.IInventoryService;

import java.util.List;

public class InventoryController {
    // Controller boundary for Inventory page and AddItem modal interactions.
    // Keeps UI logic thin by delegating state changes and queries to service layer.
    private final IInventoryService inventoryService;

    // Inject inventory service abstraction for easier testing and module isolation.
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Read model for Inventory page cards.
    public List<FoodItem> getVisibleItems() { return inventoryService.getAllItems(); }

    // Add flow entrypoint called by AddItemModal after suggestion selection.
    // PRD: name must come from catalog suggestions, not free-form user-defined item.
    public FoodItem addItem(String foodName) { return inventoryService.addItem(foodName); }

    // Inventory filter action by FoodCategory (ingredient category only).
    public List<FoodItem> filterByCategory(String categoryName) { return inventoryService.filterByCategory(categoryName); }

    // Inventory sort action for urgency-oriented ordering.
    public List<FoodItem> sortByExpiry() { return inventoryService.sortByExpiry(); }

    // Inventory sort action for newest-first browsing.
    public List<FoodItem> sortByCreatedTime() { return inventoryService.sortByCreatedTime(); }
}
