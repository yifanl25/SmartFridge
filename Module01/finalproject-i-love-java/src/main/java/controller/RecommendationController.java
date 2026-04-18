package controller;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IRecommendationService;

import java.util.List;

/**
 * Controller for the recommendation flow.
 */
public class RecommendationController {

    private final IRecommendationService recommendationService;

    public RecommendationController(IRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Returns the recommendation list for the current inventory and preference.
     */
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        return recommendationService.getRecommendations(inventory, preference);
    }

    /**
     * Filters the current recommendation list by recipe category.
     */
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return recommendationService.filterByRecipeCategory(categoryName);
    }

    /**
     * Returns one recipe by id from the current recommendation list.
     */
    public Recipe getRecommendationById(
            List<FoodItem> inventory,
            Preference preference,
            String recipeId) {
        List<Recipe> recipes = getRecommendations(inventory, preference);

        for (Recipe recipe : recipes) {
            if (recipe.getId().equals(recipeId)) {
                return recipe;
            }
        }

        return null;
    }

    /**
     * Sorts recommendations by match score.
     */
    public List<Recipe> sortByMatchScore() {
        return recommendationService.sortByMatchScore();
    }

    /**
     * Sorts recommendations by cook time.
     */
    public List<Recipe> sortByCookTime() {
        return recommendationService.sortByCookTime();
    }
}