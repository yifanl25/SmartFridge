/**
 * Represents a single ingredient that needs to be purchased.
 */
public class GroceryItem {

    private String itemId;       // matches item id in inventory, e.g. "item_002"
    private String name;         // ingredient name, e.g. "Large Eggs"
    private String category;     // category name, e.g. "Dairy & Eggs"
    private double amountNeeded; // quantity to purchase
    private String unit;         // unit, e.g. "count" / "g" / "ml"
    private double priceUsd;     // unit price from inventory
    private boolean collected;   // whether the user has checked this item off
    private String recipeId;     // which recipe this item came from
    private String recipeName;   // name of recipe

    // Constructor

    public GroceryItem(String itemId, String name, String category,
                       double amountNeeded, String unit, double priceUsd,
                       String recipeId, String recipeName) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.amountNeeded = amountNeeded;
        this.unit = unit;
        this.priceUsd = priceUsd;
        this.collected = false;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    // Getters

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getAmountNeeded() { return amountNeeded; }
    public String getUnit() { return unit; }
    public double getPriceUsd() { return priceUsd; }
    public boolean isCollected() { return collected; }
    public String getRecipeId() { return recipeId; }
    public String getRecipeName() { return recipeName; }

    // Setters

    public void setCollected(boolean collected) { this.collected = collected; }
    public void setAmountNeeded(double amountNeeded) { this.amountNeeded = amountNeeded; }

    @Override
    public String toString() {
        return String.format("GroceryItem{name='%s', category='%s', amount=%.1f %s, price=$%.2f, collected=%b}",
                name, category, amountNeeded, unit, priceUsd, collected);
    }
}
