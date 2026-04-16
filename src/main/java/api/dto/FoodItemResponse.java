package api.dto;

import model.FoodCategory;
import model.FoodItem;

/**
 * 返回给手机的「食材 JSON」/ JSON view of {@link FoodItem} for the app (uses {@code newItem} not raw word {@code new}).
 * <p>多出来的字段：先看 FoodItem 里有没有，<strong>不要</strong>加仓库位置 storageLocation。</p>
 */
public class FoodItemResponse {
    // 以后字段变了，改 from() 就行 / When FoodItem grows, update from().
    // 食材类≠菜谱类，别混在一起 / Food vs recipe categories stay separate.
    // INSERT YOUR CODE HERE

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
        FoodItemResponse r = new FoodItemResponse();
        r.id = item.getId();
        r.name = item.getName();
        r.category = item.getCategory();
        r.quantity = item.getQuantity();
        r.unit = item.getUnit();
        r.createdAt = item.getCreatedAt();
        r.expiryDate = item.getExpiryDate();
        r.newItem = item.isNew();
        r.urgent = item.isUrgent();
        return r;
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
