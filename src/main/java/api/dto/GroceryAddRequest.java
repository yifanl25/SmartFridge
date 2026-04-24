package api.dto;

/**
 * POST cart sends JSON / JSON body when adding a grocery line.
 */
public class GroceryAddRequest {
    // Add fields only when PRD says so.
    // INSERT YOUR CODE HERE

    private String foodName;
    private int quantity = 1;
    private double price;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
