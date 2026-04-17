package api.dto;

/**
 * DTO for one ingredient row on the recipe detail page.
 */
public class RecipeIngredientStatusResponse {

    private String name;
    private String quantityText;
    private boolean optional;
    private boolean inFridge;
    private String status;
    private String currentStockText;
    private String shortageText;
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
