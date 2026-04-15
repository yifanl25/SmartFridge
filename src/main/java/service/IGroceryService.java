package service;

import model.GroceryItem;

import java.util.List;

public interface IGroceryService {
    // Return current grocery rows derived from missing recipe ingredients.
    List<GroceryItem> getItems();

    // Toggle row checkbox state; only collected=true rows count into summary totals.
    GroceryItem toggleCollected(String itemId);

    // Adjust quantity by delta from + / - controls.
    GroceryItem updateQuantity(String itemId, int delta);

    // Remove grocery row from current session list.
    void deleteItem(String itemId);

    // subtotal = sum(quantity * price) for collected rows only.
    double calculateSubtotal();

    // tax = subtotal * 0.08.
    double calculateTax(double subtotal);

    // total = subtotal + tax.
    double calculateTotal(double subtotal, double tax);

    // Logical loop end hook for grocery module.
    void checkout();

    // Clear session-only grocery state.
    void clearGrocery();
}
