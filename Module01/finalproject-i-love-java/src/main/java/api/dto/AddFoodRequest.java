package api.dto;

/**
 * Request body for adding one inventory item.
 */
public class AddFoodRequest {

    private String foodName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
