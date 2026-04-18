package controller;

import model.FoodItem;
import service.IInventoryService;

import java.util.List;

/**
 * Controller for the inventory flow.
 */
public class InventoryController {

    private final IInventoryService inventoryService;

    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Returns all inventory items for display.
     */
    public List<FoodItem> getVisibleItems() {
        return inventoryService.getAllItems();
    }

    /**
     * Adds one item to the inventory.
     */
    public FoodItem addItem(String foodName) {
        return inventoryService.addItem(foodName);
    }

    /**
     * Filters inventory items by food category name.
     */
    public List<FoodItem> filterByCategory(String categoryName) {
        return inventoryService.filterByCategory(categoryName);
    }

    /**
     * Sorts inventory items by expiry date.
     */
    public List<FoodItem> sortByExpiry() {
        return inventoryService.sortByExpiry();
    }

    /**
     * Sorts inventory items by created time.
     */
    public List<FoodItem> sortByCreatedTime() {
        return inventoryService.sortByCreatedTime();
    }
}
