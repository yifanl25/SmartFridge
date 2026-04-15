package controller;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IRecommendationService;

import java.util.List;

public class RecommendationController {
    // Controller boundary for Recommendation and RecipeDetail pages.
    private final IRecommendationService recommendationService;

    // Inject recommendation service abstraction for algorithm and sorting/filter behavior.
    public RecommendationController(IRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Compute/refresh recommendations from current inventory + selected preference.
    // PRD scoring details are implemented in service layer.
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        return recommendationService.getRecommendations(inventory, preference);
    }

    // Filter recommendation cards by RecipeCategory only (never FoodCategory).
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return recommendationService.filterByRecipeCategory(categoryName);
    }

    // Sort recommendation cards by match score.
    public List<Recipe> sortByMatchScore() {
        return recommendationService.sortByMatchScore();
    }

    // Sort recommendation cards by cooking time.
    public List<Recipe> sortByCookTime() {
        return recommendationService.sortByCookTime();
    }
}
