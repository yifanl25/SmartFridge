import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BUsiness logic for the RecipeDEtail page.
 *
 * Fetch the recipe details.
 */
public class RecipeService {
    private final RecipesRepository repository;
    public RecipeService(RecipesRepository repository) {
        this.repository = repository;
    }
    // 1. get the recipes information

    /**
     * Returns all display info for a recipe.
     *
     * @param recipeId e.g. "rec_001"
     * @return recipe info Map, or null if not found
     */
    public Map<String, Object> getRecipeDetail(String recipeId) {
        RecipesRecord recipe = repository.getRecipeById(recipeId);
        if (recipe == null) {
            return null;
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("id", recipe.id());
        detail.put("name", recipe.name());
        detail.put("category", recipe.category());
        detail.put("servings", recipe.servings());
        detail.put("cookTimeMin", recipe.cookTimeMin());
        detail.put("calories", recipe.calories());
        detail.put("rating", recipe.rating());
        detail.put("priceEstimate", recipe.priceEstimateUsd());
        detail.put("protein", recipe.proteinG());
        detail.put("carbs", recipe.carbsG());
        detail.put("fiber", recipe.fiberG());
        detail.put("tags", recipe.tags());
        detail.put("instructions", recipe.instructions());

        return detail;
    }
    // 2. get ingredient list
    /**
     * Returns each ingredient with its inventory status.
     * Status can be:
     *   "from current fridge"  - enough in stock
     *   "partially available"  - in fridge but not enough
     *   "need to buy"          - not in fridge at all
     *
     * @param recipeId e.g. "rec_001"
     * @return list of ingredient Maps with status
     */
    public List<Map<String, Object>> getIngredientsWithInventoryStatus(String recipeId) {
        RecipesRecord recipe = repository.getRecipeById(recipeId);
        if (recipe == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (IngredientRecord ingredient : recipe.ingredients()) {
            RecipesRepository.InventoryItem invItem =
                    repository.getInventoryItemById(ingredient.itemId());

            Map<String, Object> info = new HashMap<>();
            info.put("itemId", ingredient.itemId());
            info.put("name", ingredient.name());
            info.put("amountNeeded", ingredient.amount());
            info.put("unit", ingredient.unit());

            if (invItem == null) {
                // not in fridge at all
                info.put("inFridge", false);
                info.put("stockSufficient", false);
                info.put("currentStock", 0.0);
                info.put("status", "need to buy");
            } else {
                boolean sufficient = invItem.quantity >= ingredient.amount();
                info.put("inFridge", true);
                info.put("stockSufficient", sufficient);
                info.put("currentStock", invItem.quantity);
                info.put("expiryDate", invItem.expiryDate);
                if (sufficient) {
                    info.put("status", "from current fridge");
                } else {
                    info.put("status", "partially available");
                    info.put("shortage", ingredient.amount() - invItem.quantity);
                }
            }
            result.add(info);
        }
        return result;
    }
    // 3. calculate ingredient match percentage
    /**
     * Calculates what percentage of ingredients are already in the fridge.
     * Used to display "94% match" badge on the recipe card.
     *
     * @param recipeId e.g. "rec_001"
     * @return match percentage 0-100
     *
     */
    public int getIngredientMatchPercent(String recipeId) {
        RecipesRecord recipe = repository.getRecipeById(recipeId);
        if (recipe == null) return 0;

        int total = 0;
        int available = 0;

        for (IngredientRecord ingredient : recipe.ingredients()) {
            total++;
            RecipesRepository.InventoryItem invItem =
                    repository.getInventoryItemById(ingredient.itemId());
            if (invItem != null && invItem.quantity >= ingredient.amount()) {
                available++;
            }
        }

        if (total == 0) return 0;
        return (int) Math.round((double) available / total * 100);
    }
}
