package service;

import model.GroceryItem;

import java.util.List;

/**
 * This is the service interface for the grocery module.
 *
 * It defines all the operations that a grocery list should support,
 * while the actual implementation is handled by GroceryService.
 *
 * This allows the controller/API layer to depend only on the interface,
 * resulting in a cleaner layered architecture.
 */
public interface IGroceryService {

    /**
     * Gets all rows which are currently on the session grocery list.
     */
    List<GroceryItem> getItems();

    /**
     * Adds one item row to the grocery list.
     */
    void addLine(GroceryItem item);

    /**
     * Filters by category.
     * <p>Teammate note: This interface is used for category-based filtering for grocery items.
     * insert your code here: keep interface in sync if service rule changes</p>
     */
    List<GroceryItem> filterByCategory(String categoryName);

    /**
     * Filter the items whose category matches the given category name.
     * <p>Teammate note: This interface is used for category-based filtering for grocery items.
     * insert your code here: keep interface in sync if service rule changes</p>
     */
    List<GroceryItem> searchByName(String keyword);

    /**
     * Toggles whether a specific item has been collected.
     *
     * Returns the updated item,
     * so the upper layer can directly send it back to the frontend.
     */
    GroceryItem toggleCollected(String itemId);

    /**
     * Updates the quantity by a given delta.
     */
    GroceryItem updateQuantity(String itemId, int delta);

    /**
     * Removes the specified item from the grocery list.
     */
    void deleteItem(String itemId);

    /**
     * Calculates the subtotal.
     *
     * Rule: only includes items that have been collected,
     * and for each item, the amount is quantity * unit price.
     */
    double calculateSubtotal();

    /**
     * Calculates the tax.
     */
    double calculateTax(double subtotal);

    /**
     * Calculates the final total from subtotal and tax.
     */
    double calculateTotal(double subtotal, double tax);

    /**
     * Finalization steps performed by the grocery module during checkout.
     */
    void checkout();

    /**
     * Clears all items from the grocery list immediately.
     */
    void clearGrocery();
}
