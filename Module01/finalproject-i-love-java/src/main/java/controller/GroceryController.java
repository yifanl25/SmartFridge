package controller;

import model.GroceryItem;
import service.IGroceryService;

import java.util.List;

/**
 * Controller for the grocery flow.
 */
public class GroceryController {

    private final IGroceryService groceryService;
    private final Runnable onCheckoutLoopEnd;

    /**
     * Creates a grocery controller with the default checkout behavior.
     */
    public GroceryController(IGroceryService groceryService) {
        this(groceryService, groceryService::checkout);
    }

    /**
     * Creates a grocery controller with a custom checkout action.
     */
    public GroceryController(IGroceryService groceryService, Runnable onCheckoutLoopEnd) {
        this.groceryService = groceryService;
        this.onCheckoutLoopEnd = onCheckoutLoopEnd;
    }

    /**
     * Returns the current grocery list.
     */
    public List<GroceryItem> getItems() {
        return groceryService.getItems();
    }

    /**
     * Adds one grocery item.
     */
    public void addLine(GroceryItem item) {
        groceryService.addLine(item);
    }

    /**
     * Filters grocery items by category.
     */
    public List<GroceryItem> filterByCategory(String categoryName) {
        return groceryService.filterByCategory(categoryName);
    }

    /**
     * Searches grocery items by name.
     */
    public List<GroceryItem> searchByName(String keyword) {
        return groceryService.searchByName(keyword);
    }

    /**
     * Toggles whether one grocery item is collected.
     */
    public GroceryItem toggleCollected(String itemId) {
        return groceryService.toggleCollected(itemId);
    }

    /**
     * Updates quantity by delta.
     */
    public GroceryItem updateQuantity(String itemId, int delta) {
        return groceryService.updateQuantity(itemId, delta);
    }

    /**
     * Deletes one grocery item.
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
     * Performs checkout.
     */
    public void checkout() {
        onCheckoutLoopEnd.run();
    }
}
