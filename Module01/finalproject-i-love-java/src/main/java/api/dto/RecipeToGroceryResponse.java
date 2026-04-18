package api.dto;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for adding missing recipe ingredients to the grocery list.
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