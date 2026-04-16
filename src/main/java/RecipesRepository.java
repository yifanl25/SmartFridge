import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Read and provide the data from recipes.json and inventory.json.
 */
public class RecipesRepository {
    // wrapper to match recipes JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RecipesWrapper {
        @JsonProperty("recipes")
        public List<RecipesRecord> recipes;
    }
    // wrapper to match inventory JSON
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
    private static class InventoryCategory {
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

    // field
    private final List<RecipesRecord> recipes;
    private final List<InventoryCategory> inventoryCategories;
    // constructor
    public RecipesRepository(String recipesPath, String inventoryPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // read recipes.json and convert to a list of Recipe objects
        RecipesWrapper recipesWrapper = mapper.readValue(new File(recipesPath), RecipesWrapper.class);
        this.recipes = recipesWrapper.recipes;

        // read inventory.json and get the list of categories
        InventoryWrapper inventoryWrapper = mapper.readValue(new File(inventoryPath), InventoryWrapper.class);
        this.inventoryCategories = inventoryWrapper.inventory.categories;
    }

    /**
     * Returns all recipes as a list.
     */
    public List<RecipesRecord> getAllRecipes() {
        return recipes;
    }

    /**
     * Returns the recipe matching the given recipeId.
     * Returns null if not found.
     */
    public RecipesRecord getRecipeById(String recipeId) {
        for (RecipesRecord recipe : recipes) {
            if (recipe.id().equals(recipeId)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Returns the recipe that comes after the current one.
     * Wraps around to the first recipe if the current one is last.
     */
    public RecipesRecord getNextRecipe(String currentRecipeId) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).id().equals(currentRecipeId)) {
                return recipes.get((i + 1) % recipes.size());
            }
        }
        return null;
    }

    // Inventory methods

    /**
     * Return the inventory item matching the given itemId.
     * Return null if not found in the fridge.
     */
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

    /**
     * Return the category name for a given itemId.
     * Return null if not found.
     */
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
}
