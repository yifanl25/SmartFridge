package service;

import model.FoodItem;

import java.util.List;

/**
 * Session-scoped inventory operations (add, query, filter, sort, clear).
 */
public interface IInventoryService {

    /**
     * Returns all items currently in memory for this session.
     */
    List<FoodItem> getAllItems();

    /**
     * Adds one item resolved from catalog by {@code foodName} (suggestion flow).
     */
    FoodItem addItem(String foodName);

    /**
     * Appends pre-built items {@link FoodItem} (e.g. demo seed from {@code data.json}).
     */
    void addAllItems(List<FoodItem> toAdd);

    /**
     * Filters by ingredient {@link model.FoodCategory} name (not recipe category).
     */
    List<FoodItem> filterByCategory(String categoryName);

    /**
     * Sorts by expiry date (earlier first for urgency browsing).
     */
    List<FoodItem> sortByExpiry();

    /**
     * Sorts by created date (newest first in implementation).
     */
    List<FoodItem> sortByCreatedTime();

    /**
     * Clears all inventory rows (checkout / session reset).
     */
    void clearInventory();
}
