import java.util.List;

/**
 * Represents the Cart Summary panel on the right side of the Grocery List page.
 * Contains subtotal, tax, total, and the list of collected items.
 */
public class CartSummary {

    private double subtotal;
    private double taxRate = 0.10;        // 10% tax
    private double estimatedTax;
    private double total;
    private List<GroceryItem> collectedItems;

    // Constructor

    public CartSummary(List<GroceryItem> collectedItems) {
        this.collectedItems = collectedItems;

        // calculate totals automatically
        this.subtotal = collectedItems.stream()
                .mapToDouble(GroceryItem::getPriceUsd)
                .sum();
        this.estimatedTax = Math.round(subtotal * taxRate * 100.0) / 100.0;
        this.total = Math.round((subtotal + estimatedTax) * 100.0) / 100.0;
    }

    // Getters

    public double getSubtotal() { return subtotal; }
    public double getEstimatedTax() { return estimatedTax; }
    public double getTotal() { return total; }
    public List<GroceryItem> getCollectedItems() { return collectedItems; }
    public int getCollectedCount() { return collectedItems.size(); }

    @Override
    public String toString() {
        return String.format("CartSummary{subtotal=$%.2f, tax=$%.2f, total=$%.2f, items=%d}",
                subtotal, estimatedTax, total, collectedItems.size());
    }
}
