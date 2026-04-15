package controller;

import model.GroceryItem;
import service.IGroceryService;

public class GroceryController {
    // Controller boundary for Grocery page interactions and summary display.
    private final IGroceryService groceryService;

    // Inject grocery service abstraction for row actions and totals.
    public GroceryController(IGroceryService groceryService) {
        this.groceryService = groceryService;
    }

    // Toggle checkbox state; only collected rows should affect subtotal.
    public GroceryItem toggleCollected(String itemId) { return groceryService.toggleCollected(itemId); }

    // Apply + / - quantity changes to a grocery row.
    public GroceryItem updateQuantity(String itemId, int delta) { return groceryService.updateQuantity(itemId, delta); }

    // Remove row from the grocery list.
    public void deleteItem(String itemId) { groceryService.deleteItem(itemId); }

    // Read subtotal based on collected rows only.
    public double calculateSubtotal() { return groceryService.calculateSubtotal(); }

    // Read tax from subtotal with fixed PRD tax rate.
    public double calculateTax(double subtotal) { return groceryService.calculateTax(subtotal); }

    // Read total = subtotal + tax.
    public double calculateTotal(double subtotal, double tax) { return groceryService.calculateTotal(subtotal, tax); }

    // Checkout action for Grocery module; app-level coordinator handles full loop reset.
    public void checkout() { groceryService.checkout(); }
}
