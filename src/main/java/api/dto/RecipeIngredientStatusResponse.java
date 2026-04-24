package api.dto;

/**
 * This class represents the status of a single ingredient in the recipe detail page.
 *
 * In simple terms:
 * A recipe usually contains multiple ingredients.
 * The frontend needs to know, for each ingredient:
 * - its name
 * - required quantity
 * - whether it is optional
 * - whether it exists in the fridge
 * - current stock amount
 * - shortage amount
 * - inventory category
 *
 * All of this information is encapsulated in this DTO.
 */
public class RecipeIngredientStatusResponse {
    // Ingredient name (e.g., Milk / Egg / Garlic)
    private String name;

    // Original quantity text from recipe (e.g., "2 count", "1 clove")
    private String quantityText;

    // Whether this ingredient is optional.
    // If true, missing it does not prevent cooking.
    private boolean optional;

    // Whether this ingredient exists in the current fridge inventory.
    private boolean inFridge;

    // Status used for frontend display.
    // Like: from current fridge / partially available / need to buy / optional
    private String status;

    // Current stock in the fridge (e.g., "1 count")
    private String currentStockText;

    // Missing quantity needed (e.g., "2 count")
    private String shortageText;

    // Category of the ingredient in inventory (e.g., Dairy / Produce)
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
