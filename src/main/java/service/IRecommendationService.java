package service;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;

import java.util.List;

public interface IRecommendationService {
    // Build recommendation list from:
    // - current inventory
    // - current preference
    // - static recipes data
    // Main matchScore must use requiredIngredients only.
    List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference);

    // Filter recommendation cards by RecipeCategory only.
    // Do not use FoodCategory for this filter.
    List<Recipe> filterByRecipeCategory(String categoryName);

    // Sort by final matchScore descending.
    // Ties should be resolved deterministically by PRD tie-break order.
    List<Recipe> sortByMatchScore();

    // Sort recommendation cards by cookTime ascending.
    List<Recipe> sortByCookTime();

    // Clear recommendation state at checkout loop end.
    void clearRecommendations();
}
