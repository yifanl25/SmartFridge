package controller;

import model.FoodItem;
import service.IInventoryService;

import java.util.List;

/**
 * Internal coordination layer for the inventory module.
 * <p>
 * This class is not a Spring web controller. It is a thin facade shared by the legacy console
 * flow and the {@code api.web} HTTP layer, while business rules remain in {@link IInventoryService}.
 */
public class InventoryController {
    /** Backing service for session inventory. / 会话库存的后端服务。 */
    private final IInventoryService inventoryService;

    /**
     * @param inventoryService injected inventory implementation / 注入的库存实现
     */
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Returns all items for list rendering.
     */
    public List<FoodItem> getVisibleItems() {
        return inventoryService.getAllItems();
    }

    /**
     * Adds one item after user picked a catalog suggestion (PRD: no arbitrary free-text add).
     */
    public FoodItem addItem(String foodName) {
        return inventoryService.addItem(foodName);
    }

    /**
     * Filters by ingredient {@link model.FoodCategory} display name (not recipe category).
     */
    public List<FoodItem> filterByCategory(String categoryName) {
        return inventoryService.filterByCategory(categoryName);
    }

    /**
     * Sorts by expiry date (earlier first).
     */
    public List<FoodItem> sortByExpiry() {
        return inventoryService.sortByExpiry();
    }

    /**
     * Sorts by created date (newest first in service).
     */
    public List<FoodItem> sortByCreatedTime() {
        return inventoryService.sortByCreatedTime();
    }
}
