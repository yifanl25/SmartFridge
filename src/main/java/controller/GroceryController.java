package controller;

import model.GroceryItem;
import service.IGroceryService;

import java.util.List;

/**
 * Internal coordination layer for grocery operations and checkout flow.
 * <p>
 * This class is intentionally thin: HTTP routing belongs in {@code api.web}, business logic stays
 * in {@link IGroceryService}, and this layer coordinates module-level actions such as session reset.
 */
public class GroceryController {
    // The service is responsible for all grocery business logic; the controller only delegates calls.
    private final IGroceryService groceryService;

    // 这是 checkout 时要执行的“收尾动作”。
    // A callback executed at the end of the checkout process.
    // By default, it only clears the grocery list.
    // In the future, this can be extended to reset inventory, preferences, or recommendations.
    private final Runnable onCheckoutLoopEnd;

    /**
     * Default constructor: checkout only clears grocery data.
     */
    public GroceryController(IGroceryService groceryService) {
        this(groceryService, groceryService::checkout);
    }

    /**
     * Custom constructor: allows injecting a more comprehensive checkout callback.
     */
    public GroceryController(IGroceryService groceryService, Runnable onCheckoutLoopEnd) {
        this.groceryService = groceryService;
        this.onCheckoutLoopEnd = onCheckoutLoopEnd;
    }

    /**
     * Gets the current grocery list.
     */
    public List<GroceryItem> getItems() {
        return groceryService.getItems();
    }

    /**
     * Adds a new grocery item.
     */
    public void addLine(GroceryItem item) {
        groceryService.addLine(item);
    }

    /**
     * Filters grocery items by category.
     */
    // ===== teammate note =====
    // The controller should only delegate calls. Do not place business rules here.
    // insert your code here only if the service signature changes
    public List<GroceryItem> filterByCategory(String categoryName) {
        return groceryService.filterByCategory(categoryName);
    }

    /**
     * Searches grocery items by keyword.
     */
    // ===== teammate note =====
    // The controller should only delegate calls. Do not place business rules here.
    // insert your code here only if the service signature changes
    public List<GroceryItem> searchByName(String keyword) {
        return groceryService.searchByName(keyword);
    }

    /**
     * Toggles whether an item is marked as collected (purchased or not).
     */
    public GroceryItem toggleCollected(String itemId) {
        return groceryService.toggleCollected(itemId);
    }

    /**
     * Updates item quantity using a delta adjustment.
     */
    public GroceryItem updateQuantity(String itemId, int delta) {
        return groceryService.updateQuantity(itemId, delta);
    }

    /**
     * Deletes a grocery item.
     */
    public void deleteItem(String itemId) {
        groceryService.deleteItem(itemId);
    }

    /**
     * Calculates subtotal.
     */
    public double calculateSubtotal() {
        return groceryService.calculateSubtotal();
    }

    /**
     * Calculates tax.
     */
    public double calculateTax(double subtotal) {
        return groceryService.calculateTax(subtotal);
    }

    /**
     * Calculates total.
     */
    public double calculateTotal(double subtotal, double tax) {
        return groceryService.calculateTotal(subtotal, tax);
    }

    /**
     * Executes checkout process.
     *
     * Instead of hardcoding a specific reset behavior,
     * this method executes the injected Runnable callback.
     * This makes the checkout process flexible and extensible.
     */
    public void checkout() {
        onCheckoutLoopEnd.run();
    }
}
