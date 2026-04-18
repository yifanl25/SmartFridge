package api.dto;

/**
 * POST /api/inventory 时发来的 JSON / JSON body when adding food over HTTP.
 * <p>现在只有一个字段：食物名字 / Right now only food name.</p>
 */
public class AddFoodRequest {
    // 如果 PRD 以后说「还要传数量、单位…」：先改 InventoryController.addItem 那一套，再在这里加字段。
    // If PRD later needs qty/unit: change InventoryController first, then add fields here.
    // 不要加「仓库位置」到 FoodItem / Never add storageLocation on FoodItem.
    // INSERT YOUR CODE HERE

    private String foodName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
