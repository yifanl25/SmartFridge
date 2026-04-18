package api.dto;

/**
 * 这个类代表 recipe detail 页面里“某一条 ingredient 的状态”。
 *
 * 大白话：
 * 一道菜通常会列很多食材，
 * 前端需要知道每一条食材：
 * - 叫什么
 * - 需要多少
 * - 是不是 optional
 * - 冰箱里有没有
 * - 当前库存多少
 * - 还差多少
 * - 属于哪个库存分类
 *
 * 这些小字段就都放在这里。
 */
public class RecipeIngredientStatusResponse {
    // 食材名，比如 Milk / Egg / Garlic
    private String name;

    // 菜谱里原始写的数量文本，比如 "2 count"、"1 clove"
    private String quantityText;

    // 这条是不是可选食材。
    // true 的话，缺了也不一定要买。
    private boolean optional;

    // 当前冰箱里有没有找到对应食材。
    private boolean inFridge;

    // 前端展示用的状态词。
    // 常见值：from current fridge / partially available / need to buy / optional
    private String status;

    // 当前库存文本，比如 "1 count"
    private String currentStockText;

    // 还差多少，比如 "2 count"
    private String shortageText;

    // 这项库存属于哪个分类，比如 Dairy / Produce
    private String inventoryCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantityText() {
        return quantityText;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isInFridge() {
        return inFridge;
    }

    public void setInFridge(boolean inFridge) {
        this.inFridge = inFridge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentStockText() {
        return currentStockText;
    }

    public void setCurrentStockText(String currentStockText) {
        this.currentStockText = currentStockText;
    }

    public String getShortageText() {
        return shortageText;
    }

    public void setShortageText(String shortageText) {
        this.shortageText = shortageText;
    }

    public String getInventoryCategory() {
        return inventoryCategory;
    }

    public void setInventoryCategory(String inventoryCategory) {
        this.inventoryCategory = inventoryCategory;
    }
}
