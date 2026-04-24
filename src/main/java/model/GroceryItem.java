package model;

/**
 * One mutable row on the session grocery list (missing-ingredient checkout flow).
 * <p>
 * representing the cycle from missing ingredients to shopping and checkout.
 */
public class GroceryItem {
    /** Row id for toggles and quantity updates.  */
    private final String id;
    /** Display name (usually missing ingredient name).  */
    private final String name;
    /** Ingredient category for grouping in UI.  */
    private final FoodCategory category;
    /** Mutable quantity.  */
    private int quantity;
    /** Unit price for subtotal = sum(qty * price) when collected.  */
    private final double price;
    /** Whether this row counts toward subtotal/tax/total.  */
    private boolean collected;

    /**
     * Creates a grocery row for the current session.
     */
    public GroceryItem(
            String id,
            String name,
            FoodCategory category,
            int quantity,
            double price,
            boolean collected) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.collected = collected;
    }

    /** Returns row id.  */
    public String getId() {
        return id;
    }

    /** Returns display name.  */
    public String getName() {
        return name;
    }

    /** Returns ingredient category.  */
    public FoodCategory getCategory() {
        return category;
    }

    /** Returns quantity.  */
    public int getQuantity() {
        return quantity;
    }

    /** Returns unit price.  */
    public double getPrice() {
        return price;
    }

    /** Returns whether the row is marked collected for checkout math.  */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Updates quantity (service enforces non-negative floor).
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Updates collected flag for subtotal inclusion.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
