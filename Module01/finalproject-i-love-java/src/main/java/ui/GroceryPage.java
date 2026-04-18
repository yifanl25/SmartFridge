package ui;

import controller.GroceryController;
import model.GroceryItem;

import java.util.List;
import java.util.Locale;

/**
 * Grocery page demo.
 */
public class GroceryPage {
    private final GroceryController groceryController;

    public GroceryPage(GroceryController groceryController) {
        this.groceryController = groceryController;
    }

    public void render() {
        System.out.println("[Grocery Page]");
        List<GroceryItem> items = groceryController.getItems();
        if (items.isEmpty()) {
            System.out.println("Grocery list is currently empty. Use recommendation -> grocery to fill it.");
            return;
        }
        for (GroceryItem item : items) {
            System.out.println("- " + item.getName() + " | qty " + item.getQuantity()
                    + " | price $" + String.format(Locale.ROOT, "%.2f", item.getPrice())
                    + " | collected=" + item.isCollected());
        }
        double subtotal = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(subtotal);
        double total = groceryController.calculateTotal(subtotal, tax);
        System.out.println("Subtotal: $" + String.format(Locale.ROOT, "%.2f", subtotal));
        System.out.println("Tax:      $" + String.format(Locale.ROOT, "%.2f", tax));
        System.out.println("Total:    $" + String.format(Locale.ROOT, "%.2f", total));
    }
}
