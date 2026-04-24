package api.dto;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Response returned after adding a recipe to the grocery list.
 *
 * In simple terms:
 * When the frontend clicks "add missing ingredients to grocery list",
 * it does not only need to know whether the operation succeeded.
 *
 * It also needs to understand what actually happened, such as:
 * - which recipe triggered the operation
 * - how many new items were added
 * - how many items were merged with existing ones
 * - which items were newly created
 * - which items were merged and consolidated
 *
 * Therefore, we design a dedicated response object instead of returning
 * a simple boolean result.
 *
 * <p>Team note:
 * This DTO is designed to describe the actual changes caused by
 * adding a recipe to the grocery list.
 *
 * If needed in the future, additional fields such as skippedItems,
 * errorItems, or unitSummary can be added here.
 * Only extend this response when the UI has a real requirement for it.
 * </p>
 * insert your code here: add extra response fields only if UI really needs them</p>
 */
public class RecipeToGroceryResponse {
    private String recipeId;
    private String recipeTitle;
    private int addedCount;
    private int mergedCount;
    private List<GroceryItem> addedItems = new ArrayList<>();
    private List<String> mergedItemNames = new ArrayList<>();

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(int addedCount) {
        this.addedCount = addedCount;
    }

    public int getMergedCount() {
        return mergedCount;
    }

    public void setMergedCount(int mergedCount) {
        this.mergedCount = mergedCount;
    }

    public List<GroceryItem> getAddedItems() {
        return new ArrayList<>(addedItems);
    }

    public void setAddedItems(List<GroceryItem> addedItems) {
        this.addedItems = new ArrayList<>(addedItems);
    }

    public List<String> getMergedItemNames() {
        return new ArrayList<>(mergedItemNames);
    }

    public void setMergedItemNames(List<String> mergedItemNames) {
        this.mergedItemNames = new ArrayList<>(mergedItemNames);
    }
}
