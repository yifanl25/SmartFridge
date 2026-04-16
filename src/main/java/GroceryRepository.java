import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Reads inventory.json and recipes.json to support the Grocery List feature.
 */
public class GroceryRepository {

    // Wrapper classes to match JSON

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RecipesWrapper {
        @JsonProperty("recipes")
        public List<RecipesRecord> recipes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class InventoryWrapper {
        @JsonProperty("inventory")
        public InventoryData inventory;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class InventoryData {
        @JsonProperty("categories")
        public List<InventoryCategory> categories;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InventoryCategory {
        @JsonProperty("name")
        public String name;

        @JsonProperty("items")
        public List<InventoryItem> items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InventoryItem {
        @JsonProperty("id") public String id;
        @JsonProperty("name") public String name;
        @JsonProperty("quantity") public double quantity;
        @JsonProperty("unit") public String unit;
        @JsonProperty("price_usd") public double priceUsd;
        @JsonProperty("expiry_date") public String expiryDate;
    }

    // Fields

    private final List<RecipesRecord> recipes;
    private final List<InventoryCategory> inventoryCategories;

    // Constructor

    public GroceryRepository(String recipesPath, String inventoryPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RecipesWrapper recipesWrapper = mapper.readValue(new File(recipesPath), RecipesWrapper.class);
        this.recipes = recipesWrapper.recipes;

        InventoryWrapper inventoryWrapper = mapper.readValue(new File(inventoryPath), InventoryWrapper.class);
        this.inventoryCategories = inventoryWrapper.inventory.categories;
    }

    // Recipe methods

    public RecipesRecord getRecipeById(String recipeId) {
        for (RecipesRecord recipe : recipes) {
            if (recipe.id().equals(recipeId)) {
                return recipe;
            }
        }
        return null;
    }

    // Inventory methods

    public InventoryItem getInventoryItemById(String itemId) {
        for (InventoryCategory category : inventoryCategories) {
            for (InventoryItem item : category.items) {
                if (item.id.equals(itemId)) {
                    return item;
                }
            }
        }
        return null;
    }

    public String getCategoryByItemId(String itemId) {
        for (InventoryCategory category : inventoryCategories) {
            for (InventoryItem item : category.items) {
                if (item.id.equals(itemId)) {
                    return category.name;
                }
            }
        }
        return null;
    }

    public List<InventoryCategory> getAllCategories() {
        return inventoryCategories;
    }
}
