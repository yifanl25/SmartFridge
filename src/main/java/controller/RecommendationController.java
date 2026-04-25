package controller;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IRecommendationService;

import java.util.List;

/**
 * Internal coordination layer for recommendation use cases.
 * <p>
 * The HTTP layer calls into this facade, and the actual scoring/cache logic stays in
 * {@link IRecommendationService}. This preserves the older controller/service split without
 * making {@code controller} itself responsible for HTTP routing.
 */
public class RecommendationController {
    // The real responsibility for recommending logic is recommendationService.
    private final IRecommendationService recommendationService;

    public RecommendationController(IRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Generates and returns a list of recommended recipes
     * based on the current inventory and user preference.
     */
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        return recommendationService.getRecommendations(inventory, preference);
    }

    /**
     * Filters the current cached recommendation list by recipe category.
     *
     * Note: This filters by RecipeCategory,
     * not FoodCategory from the inventory domain.
     */
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return recommendationService.filterByRecipeCategory(categoryName);
    }

    /**
     * Retrieves a single recommended recipe by its ID
     * from the current recommendation result set.
     *
     * This method is used by the recipe detail endpoint.
     */
    public Recipe getRecommendationById(List<FoodItem> inventory, Preference preference, String recipeId) {
        return getRecommendations(inventory, preference).stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sorts by match score.
     */
    public List<Recipe> sortByMatchScore() {
        return recommendationService.sortByMatchScore();
    }

    /**
     * Sorts by cook time.
     */
    public List<Recipe> sortByCookTime() {
        return recommendationService.sortByCookTime();
    }
}
