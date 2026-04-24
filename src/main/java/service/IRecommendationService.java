package service;

import model.FoodItem;
import model.Preference;
import model.Recipe;

import java.util.List;

/**
 * Computes and stores scored recipe recommendations for the current session.
 * <p>
 */
public interface IRecommendationService {

    /**
     * Scores each template recipe from inventory + preference; refreshes cached list and returns a copy.
     * Main coverage uses required ingredients only (per PRD).
     * <p>
     */
    List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference);

    /**
     * Filters cached recommendations by {@link model.RecipeCategory} name substring (case-insensitive).
     * Do not use {@link model.FoodCategory} here.
     * <p>
     */
    List<Recipe> filterByRecipeCategory(String categoryName);

    /**
     * Returns cached recommendations sorted by match score descending with PRD tie-breaks.
     * <p>
     */
    List<Recipe> sortByMatchScore();

    /**
     * Returns cached recommendations sorted primarily by cook time ascending.
     * <p>
     */
    List<Recipe> sortByCookTime();

    /**
     * Clears cached recommendations at checkout / session reset.
     * <p>
     */
    void clearRecommendations();
}
