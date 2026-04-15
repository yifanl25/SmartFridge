package service;

import model.FoodItem;

import java.util.List;

public interface IInventoryService {
    // Return current in-session inventory snapshot.
    // Data must be cleared after checkout in the app loop.
    List<FoodItem> getAllItems();

    // Add one inventory item from a selected suggestion only.
    // PRD rules: quantity defaults to 1; createdAt is now; expiry from catalog defaultExpiryDays.
    FoodItem addItem(String foodName);

    // Filter inventory by FoodCategory (ingredient category), not recipe category.
    List<FoodItem> filterByCategory(String categoryName);

    // Sort by expiry urgency timeline for inventory operations.
    List<FoodItem> sortByExpiry();

    // Sort by createdAt so newest items can be shown first.
    List<FoodItem> sortByCreatedTime();

    // Clear session-only inventory state at loop end (checkout reset behavior).
    void clearInventory();
}
