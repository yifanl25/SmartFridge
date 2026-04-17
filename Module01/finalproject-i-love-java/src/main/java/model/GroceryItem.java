package model;

/**
 * Represents one grocery item in the current session.
 */
public class GroceryItem {

    private final String id;
    private final String name;
    private final FoodCategory category;
    private int quantity;
    private final double price;
    private boolean collected;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
