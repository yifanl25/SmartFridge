package api.web;

import api.dto.RecipeDetailResponse;
import api.dto.RecipeToGroceryResponse;
import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;
import model.GroceryItem;
import model.Preference;
import model.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IFoodCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles HTTP requests for recipe recommendations and recipe detail.
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationApiController {

    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;
    private final IFoodCatalog foodCatalog;

    public RecommendationApiController(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController,
            IFoodCatalog foodCatalog) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
        this.foodCatalog = foodCatalog;
    }

    /**
     * Returns the recommendation list.
     *
     * Supported query parameters:
     * - category
     * - sort=score
     * - sort=cookTime
     */
    @GetMapping
    public List<Recipe> list(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false) String sort) {

        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        List<Recipe> recipes = recommendationController.getRecommendations(inventory, preference);

        if (sort != null && !sort.isBlank()) {
            if ("cookTime".equalsIgnoreCase(sort)) {
                recipes = recommendationController.sortByCookTime();
            } else if ("score".equalsIgnoreCase(sort)) {
                recipes = recommendationController.sortByMatchScore();
            }
        }

        if (category == null || category.isBlank()) {
            return recipes;
        }

        String targetCategory = category.trim().toLowerCase(Locale.ROOT);
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            String recipeCategory = recipe.getRecipeCategory().getName().toLowerCase(Locale.ROOT);
            if (recipeCategory.contains(targetCategory)) {
                filtered.add(recipe);
            }
        }

        return filtered;
    }

    /**
     * Returns detail for one recipe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailResponse> detail(@PathVariable String id) {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        Recipe recipe = recommendationController.getRecommendationById(inventory, preference, id);

        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeDetailResponse response =
                RecipeDetailResponse.from(recipe, inventory, foodCatalog);
        return ResponseEntity.ok(response);
    }

    /**
     * Adds missing required ingredients from one recipe to the grocery list.
     */
    @PostMapping("/{id}/grocery")
    public ResponseEntity<RecipeToGroceryResponse> addMissingIngredientsToGrocery(
            @PathVariable String id) {

        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        Recipe recipe = recommendationController.getRecommendationById(inventory, preference, id);

        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeToGroceryResponse response = new RecipeToGroceryResponse();
        response.setRecipeId(recipe.getId());
        response.setRecipeTitle(recipe.getTitle());

        List<GroceryItem> addedItems = new ArrayList<>();
        List<String> mergedItemNames = new ArrayList<>();

        for (Recipe.Ingredient ingredient : recipe.getRequiredIngredients()) {
            if (!isMissingRequiredIngredient(recipe, ingredient)) {
                continue;
            }

            String canonicalName = foodCatalog.canonicalFoodName(ingredient.getName());
            int neededQuantity = parseQuantityAsPositiveInt(ingredient.getQuantityText());

            GroceryItem existing = findExistingGroceryItem(canonicalName);
            if (existing != null) {
                groceryController.updateQuantity(existing.getId(), neededQuantity);
                mergedItemNames.add(existing.getName());
                continue;
            }

            FoodCatalogEntry entry = resolveEntry(canonicalName);
            FoodCategory category;

            if (entry == null) {
                category = new FoodCategory("misc", "Misc", "box");
            } else {
                category = entry.getCategory();
            }

            GroceryItem newItem = new GroceryItem(
                    UUID.randomUUID().toString(),
                    canonicalName,
                    category,
                    neededQuantity,
                    0.0,
                    false
            );

            groceryController.addLine(newItem);
            addedItems.add(newItem);
        }

        response.setAddedItems(addedItems);
        response.setAddedCount(addedItems.size());
        response.setMergedItemNames(mergedItemNames);
        response.setMergedCount(mergedItemNames.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Returns true if this required ingredient is currently missing.
     */
    private boolean isMissingRequiredIngredient(Recipe recipe, Recipe.Ingredient ingredient) {
        String target = norm(foodCatalog.canonicalFoodName(ingredient.getName()));

        for (String missing : recipe.getMissingIngredients()) {
            String normalizedMissing = norm(foodCatalog.canonicalFoodName(missing));
            if (normalizedMissing.equals(target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns an existing grocery item with the same normalized name.
     */
    private GroceryItem findExistingGroceryItem(String canonicalName) {
        String key = norm(canonicalName);
        List<GroceryItem> items = groceryController.getItems();

        for (GroceryItem item : items) {
            String itemName = norm(foodCatalog.canonicalFoodName(item.getName()));
            if (itemName.equals(key)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Parses quantity text and returns a positive integer.
     * Returns 1 if parsing fails.
     */
    private int parseQuantityAsPositiveInt(String quantityText) {
        if (quantityText == null || quantityText.isBlank()) {
            return 1;
        }

        String[] parts = quantityText.trim().split("\\s+", 2);

        try {
            double value = Double.parseDouble(parts[0]);
            int quantity = (int) Math.ceil(value);
            return Math.max(1, quantity);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Tries to resolve a food name to one catalog entry.
     */
    private FoodCatalogEntry resolveEntry(String foodName) {
        Optional<FoodCatalogEntry> direct = foodCatalog.resolveEntry(foodName);
        if (direct.isPresent()) {
            return direct.get();
        }

        List<FoodCatalogEntry> suggestions = foodCatalog.searchSuggestions(foodName);
        if (suggestions.isEmpty()) {
            return null;
        }

        return suggestions.get(0);
    }

    /**
     * Normalizes text for comparison.
     */
    private static String norm(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }
}