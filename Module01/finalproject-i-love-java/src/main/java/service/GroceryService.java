package service;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stores and updates the grocery list for the current session.
 */
public class GroceryService implements IGroceryService {

    private static final double TAX_RATE = 0.08;

    private final List<GroceryItem> groceryItems;

    public GroceryService(List<GroceryItem> items) {
        this.groceryItems = new ArrayList<>(items);
    }

    /**
     * Returns the current grocery list.
     */
    @Override
    public List<GroceryItem> getItems() {
        return new ArrayList<>(groceryItems);
    }

    /**
     * Adds one grocery item.
     */
    @Override
    public void addLine(GroceryItem item) {
        groceryItems.add(item);
    }

    /**
     * Filters grocery items by category name.
     */
    @Override
    public List<GroceryItem> filterByCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()
                || categoryName.equalsIgnoreCase("All")) {
            return new ArrayList<>(groceryItems);
        }

        String target = categoryName.trim().toLowerCase(Locale.ROOT);
        List<GroceryItem> result = new ArrayList<>();

        for (GroceryItem item : groceryItems) {
            String itemCategory = item.getCategory().getName().toLowerCase(Locale.ROOT);
            if (itemCategory.equals(target)) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Searches grocery items by name.
     */
    @Override
    public List<GroceryItem> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(groceryItems);
        }

        String target = keyword.trim().toLowerCase(Locale.ROOT);
        List<GroceryItem> result = new ArrayList<>();

        for (GroceryItem item : groceryItems) {
            String itemName = item.getName().toLowerCase(Locale.ROOT);
            if (itemName.contains(target)) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Toggles whether one grocery item is collected.
     */
    @Override
    public GroceryItem toggleCollected(String itemId) {
        GroceryItem item = findById(itemId);
        item.setCollected(!item.isCollected());
        return item;
    }

    /**
     * Updates quantity by delta.
     */
    @Override
    public GroceryItem updateQuantity(String itemId, int delta) {
        GroceryItem item = findById(itemId);
        item.setQuantity(Math.max(0, item.getQuantity() + delta));
        return item;
    }

    /**
     * Deletes one grocery item.
     */
    @Override
    public void deleteItem(String itemId) {
        groceryItems.removeIf(item -> item.getId().equals(itemId));
    }

    /**
     * Calculates subtotal using collected items only.
     */
    @Override
    public double calculateSubtotal() {
        double subtotal = 0.0;

        for (GroceryItem item : groceryItems) {
            if (item.isCollected()) {
                subtotal += item.getPrice() * item.getQuantity();
            }
        }

        return subtotal;
    }

    /**
     * Calculates tax from the subtotal.
     */
    @Override
    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    /**
     * Calculates total from subtotal and tax.
     */
    @Override
    public double calculateTotal(double subtotal, double tax) {
        return subtotal + tax;
    }

    /**
     * Performs checkout for the grocery module.
     */
    @Override
    public void checkout() {
        clearGrocery();
    }

    /**
     * Clears the grocery list.
     */
    @Override
    public void clearGrocery() {
        groceryItems.clear();
    }

    /**
     * Returns one grocery item by id.
     */
    private GroceryItem findById(String itemId) {
        for (GroceryItem item : groceryItems) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Item not found: " + itemId);
    }
}
