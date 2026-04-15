package service;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroceryService implements IGroceryService {
    // PRD fixed tax rate.
    private static final double TAX_RATE = 0.08;
    // Session-only grocery list generated from missing ingredients.
    private final List<GroceryItem> groceryItems;

    // Construct grocery state for current loop only.
    public GroceryService(List<GroceryItem> items) {
        this.groceryItems = new ArrayList<>(items);
    }

    @Override
    // Return defensive copy of current grocery rows.
    public List<GroceryItem> getItems() {
        return new ArrayList<>(groceryItems);
    }

    @Override
    // Toggle collection checkbox state for a grocery row.
    public GroceryItem toggleCollected(String itemId) {
        GroceryItem item = findById(itemId);
        item.setCollected(!item.isCollected());
        return item;
    }

    @Override
    // Increase/decrease quantity by delta; quantity floor is 0.
    public GroceryItem updateQuantity(String itemId, int delta) {
        GroceryItem item = findById(itemId);
        item.setQuantity(Math.max(0, item.getQuantity() + delta));
        return item;
    }

    @Override
    // Remove one grocery row from the session list.
    public void deleteItem(String itemId) {
        groceryItems.removeIf(i -> i.getId().equals(itemId));
    }

    @Override
    // PRD summary rule:
    // subtotal counts only collected items.
    public double calculateSubtotal() {
        return groceryItems.stream()
                .filter(GroceryItem::isCollected)
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    @Override
    // PRD formula: tax = subtotal * 0.08.
    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    @Override
    // PRD formula: total = subtotal + tax.
    public double calculateTotal(double subtotal, double tax) {
        return subtotal + tax;
    }

    @Override
    // Module-local checkout behavior clears grocery state.
    // Full app loop reset (preference/inventory/recommendations/grocery) is coordinated above this layer.
    public void checkout() {
        clearGrocery();
    }

    @Override
    // Clear all grocery rows for session reset.
    public void clearGrocery() {
        groceryItems.clear();
    }

    // Internal row lookup helper with explicit failure on missing id.
    private GroceryItem findById(String itemId) {
        Optional<GroceryItem> item = groceryItems.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
        return item.orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
    }
}
