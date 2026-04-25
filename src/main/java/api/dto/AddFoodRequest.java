package api.dto;

/**
 * JSON request body when adding food over HTTP(POST /api/inventory).
 * <p>Right now only food name.</p>
 */
public class AddFoodRequest {

    // If PRD later needs qty/unit: change InventoryController first, then add fields here.
    // Never add storageLocation on FoodItem.
    // INSERT YOUR CODE HERE

    private String foodName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
