package model;

public class GroceryItem {
    // Session-only grocery row id generated from missing-ingredient flow.
    private final String id;
    // Grocery display name (normally missing required ingredient name).
    private final String name;
    // Ingredient category for grocery grouping/filter visuals.
    private final FoodCategory category;
    // Mutable quantity adjusted by +/- controls on Grocery page.
    private int quantity;
    // Unit price used in subtotal = sum(quantity * price) for collected rows.
    private final double price;
    // PRD: only collected=true items contribute to subtotal/tax/total.
    private boolean collected;

    // Constructs a mutable grocery row for in-session checkout workflow.
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

    // Returns grocery item id.
    public String getId() { return id; }
    // Returns grocery item display name.
    public String getName() { return name; }
    // Returns ingredient category.
    public FoodCategory getCategory() { return category; }
    // Returns current quantity value.
    public int getQuantity() { return quantity; }
    // Returns unit price.
    public double getPrice() { return price; }
    // Returns collection state used in summary calculation filters.
    public boolean isCollected() { return collected; }

    // Updates quantity from plus/minus interactions (service should guard min bounds).
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Toggles checkbox state for subtotal inclusion.
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
