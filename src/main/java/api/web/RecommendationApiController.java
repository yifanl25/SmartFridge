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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.IFoodCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for recipe recommendations and recipe-related actions.
 * <p>
 * Exposes three endpoints:
 * <ul>
 *   <li>GET  /api/recommendations          — filtered and sorted recommendation list</li>
 *   <li>GET  /api/recommendations/{id}     — recipe detail page data</li>
 *   <li>POST /api/recommendations/{id}/grocery — add missing ingredients to grocery list</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationApiController {

    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;
    private final IFoodCatalog foodCatalog;

    /**
     * Constructs the controller with all required dependencies.
     *
     * @param inventoryController      provides the current fridge inventory
     * @param preferenceController     provides the current user preference
     * @param recommendationController scores and sorts recipe recommendations
     * @param groceryController        manages the grocery list
     * @param foodCatalog              resolves and normalizes food names
     */
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
     * Returns a filtered and sorted list of recipe recommendations.
     * <p>
     * Scores are recalculated on every request based on the latest inventory
     * and preference state, so changes made by the user are immediately reflected.
     * <p>
     * Supported query parameters:
     * <ul>
     *   <li>{@code sort} — {@code "cookTime"} sorts by fastest cook time;
     *       any other value sorts by match score; omitting keeps default order.</li>
     *   <li>{@code category} — filters results to recipes whose category name
     *       contains the given string (case-insensitive, partial match).</li>
     * </ul>
     *
     * @param category optional recipe category filter (e.g. "Breakfast")
     * @param sort     optional sort mode: {@code "cookTime"} or {@code "score"}
     * @return filtered and sorted list of recipes
     */
    @GetMapping
    public List<Recipe> list(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false) String sort) {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        List<Recipe> base = recommendationController.getRecommendations(inventory, preference);

        if (sort != null && !sort.isBlank()) {
            if ("cookTime".equalsIgnoreCase(sort)) {
                base = recommendationController.sortByCookTime();
            } else {
                base = recommendationController.sortByMatchScore();
            }
        }

        if (category == null || category.isBlank()) {
            return base;
        }

        String needle = category.trim().toLowerCase(Locale.ROOT);
        return base.stream()
                .filter(r -> r.getRecipeCategory().getName().toLowerCase(Locale.ROOT).contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * Returns the full detail of a single recipe by its ID.
     * <p>
     * Used by the Recipe Detail page. Returns the recipe's fields along with
     * ingredient availability status based on the current inventory.
     * Returns 404 if no recipe with the given ID is found.
     *
     * @param id the recipe ID from the URL path
     * @return 200 with {@link RecipeDetailResponse}, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailResponse> detail(@PathVariable String id) {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        Recipe recipe = recommendationController.getRecommendationById(inventory, preference, id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(RecipeDetailResponse.from(recipe, inventory, foodCatalog));
    }

    /**
     * Adds all missing required ingredients of a recipe to the grocery list.
     * <p>
     * This closes the core PRD loop: recommendation → recipe detail → grocery planning.
     * <p>
     * Two key behaviors:
     * <ul>
     *   <li>Parses quantity from the ingredient's quantity text (e.g. "2 count" → 2).</li>
     *   <li>If the grocery list already contains the same item, merges the quantity
     *       instead of inserting a duplicate row.</li>
     * </ul>
     * Optional ingredients are never added to the grocery list.
     * Returns 404 if the recipe ID is not found.
     *
     * @param id the recipe ID from the URL path
     * @return 200 with {@link RecipeToGroceryResponse} summarizing added and merged items,
     * or 404 if not found
     */
    @PostMapping("/{id}/grocery")
    public ResponseEntity<RecipeToGroceryResponse> addMissingIngredientsToGrocery(@PathVariable String id) {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        Recipe recipe = recommendationController.getRecommendationById(inventory, preference, id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeToGroceryResponse response = new RecipeToGroceryResponse();
        response.setRecipeId(recipe.getId());
        response.setRecipeTitle(recipe.getTitle());

        List<GroceryItem> added = new ArrayList<>();
        List<String> merged = new ArrayList<>();

        for (Recipe.Ingredient ingredient : recipe.getRequiredIngredients()) {
            if (!isMissingRequiredIngredient(recipe, ingredient)) {
                continue;
            }

            String canonical = foodCatalog.canonicalFoodName(ingredient.getName());

            int neededQty = parseQuantityAsPositiveInt(ingredient.getQuantityText());

            GroceryItem existing = findExistingGroceryItem(canonical);
            if (existing != null) {
                groceryController.updateQuantity(existing.getId(), neededQty);
                merged.add(existing.getName());
                continue;
            }

            FoodCatalogEntry entry = resolveEntry(canonical);
            FoodCategory category = entry == null
                    ? new FoodCategory("misc", "Misc", "box")
                    : entry.getCategory();

            GroceryItem line = new GroceryItem(
                    UUID.randomUUID().toString(),
                    canonical,
                    category,
                    neededQty,
                    0.0,
                    false);
            groceryController.addLine(line);
            added.add(line);
        }

        response.setAddedItems(added);
        response.setAddedCount(added.size());
        response.setMergedItemNames(merged);
        response.setMergedCount(merged.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Returns true if the given required ingredient is in the recipe's missing ingredients list.
     * <p>
     * Compares canonical names rather than raw strings to avoid mismatches
     * caused by aliases (e.g. "milk" vs "whole milk").
     *
     * @param recipe     the scored recipe
     * @param ingredient the ingredient to check
     * @return true if the ingredient is missing from the user's inventory
     */
    private boolean isMissingRequiredIngredient(Recipe recipe, Recipe.Ingredient ingredient) {
        String target = norm(foodCatalog.canonicalFoodName(ingredient.getName()));
        return recipe.getMissingIngredients().stream()
                .map(foodCatalog::canonicalFoodName)
                .map(RecommendationApiController::norm)
                .anyMatch(target::equals);
    }

    /**
     * Finds an existing grocery item whose canonical name matches the given name.
     * <p>
     * Uses canonical name comparison to avoid duplicates caused by name variants
     * (e.g. "milk", "whole milk", "dairy milk").
     *
     * @param canonicalName the normalized food name to search for
     * @return the matching {@link GroceryItem}, or {@code null} if not found
     */
    private GroceryItem findExistingGroceryItem(String canonicalName) {
        String key = norm(canonicalName);
        return groceryController.getItems().stream()
                .filter(item -> norm(foodCatalog.canonicalFoodName(item.getName())).equals(key))
                .findFirst()
                .orElse(null);
    }

    /**
     * Parses a quantity text string into a positive integer.
     * <p>
     * Examples:
     * <ul>
     *   <li>"2 count" → 2</li>
     *   <li>"1.5 cup" → 2 (rounded up)</li>
     *   <li>unreadable or null → 1</li>
     * </ul>
     *
     * @param quantityText the raw quantity string from the recipe ingredient
     * @return a positive integer quantity, defaulting to 1 if parsing fails
     */
    private int parseQuantityAsPositiveInt(String quantityText) {
        if (quantityText == null || quantityText.isBlank()) {
            return 1;
        }
        String[] parts = quantityText.trim().split("\\s+", 2);
        try {
            double parsed = Double.parseDouble(parts[0]);
            return Math.max(1, (int) Math.ceil(parsed));
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }

    /**
     * Resolves a food name to a {@link FoodCatalogEntry}.
     * <p>
     * First attempts a direct lookup; if that fails, falls back to the first
     * suggestion returned by the catalog search.
     *
     * @param foodName the food name to resolve
     * @return a matching {@link FoodCatalogEntry}, or {@code null} if none found
     */
    private FoodCatalogEntry resolveEntry(String foodName) {
        Optional<FoodCatalogEntry> direct = foodCatalog.resolveEntry(foodName);
        if (direct.isPresent()) {
            return direct.get();
        }
        return foodCatalog.searchSuggestions(foodName).stream().findFirst().orElse(null);
    }


    /**
     * Normalizes a string for comparison by trimming whitespace and converting to lowercase.
     *
     * @param s the string to normalize
     * @return normalized string, or empty string if input is null
     */
    private static String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }
}
