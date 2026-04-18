package api.dto;

import model.FoodCategory;
import model.FoodItem;

/**
 * Response DTO for one inventory item.
 */
public class FoodItemResponse {

    private String id;
    private String name;
    private FoodCategory category;
    private int quantity;
    private String unit;
    private String createdAt;
    private String expiryDate;
    private boolean newItem;
    private boolean urgent;

    public static FoodItemResponse from(FoodItem item) {
        FoodItemResponse response = new FoodItemResponse();
        response.id = item.getId();
        response.name = item.getName();
        response.category = item.getCategory();
        response.quantity = item.getQuantity();
        response.unit = item.getUnit();
        response.createdAt = item.getCreatedAt();
        response.expiryDate = item.getExpiryDate();
        response.newItem = item.isNew();
        response.urgent = item.isUrgent();
        return response;
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

    public String getUnit() {
        return unit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public boolean isUrgent() {
        return urgent;
    }
}