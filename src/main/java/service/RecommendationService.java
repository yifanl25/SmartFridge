package service;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RecommendationService implements IRecommendationService {
    // Static recipe dataset loaded at startup from recipes.json.
    private final List<Recipe> recipes;
    // Session-only recommendation result cache for current loop.
    private List<Recipe> currentRecommendations;

    // Construct recommendation service with immutable recipe source snapshot.
    public RecommendationService(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
        this.currentRecommendations = new ArrayList<>();
    }

    @Override
    // Build recommendation results for current inventory + preference context.
    // PRD target implementation detail (to be fully implemented in this method):
    // 1) normalize names (lowercase + trim + alias canonicalization)
    // 2) match requiredIngredients only for main score
    // 3) compute matchScore = coverage(0-60)+urgent(0-20)+alignment(0-15)+convenience(0-5)
    // 4) apply deterministic tie-break: missing asc, urgent matched desc, cookTime asc, rating desc, title asc
    // Note: current body is placeholder behavior; comments define the required final algorithm.
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        currentRecommendations = recipes.stream()
                .filter(r -> !r.getAvailableIngredients().isEmpty() || !inventory.isEmpty())
                .collect(Collectors.toList());
        return new ArrayList<>(currentRecommendations);
    }

    @Override
    // Filter recommendation set by RecipeCategory only.
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return currentRecommendations.stream()
                .filter(r -> r.getRecipeCategory().getName().toLowerCase(Locale.ROOT)
                        .contains(categoryName.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    @Override
    // Sort by computed matchScore descending.
    // For full PRD compliance, chain tie-break comparator after score comparator.
    public List<Recipe> sortByMatchScore() {
        return currentRecommendations.stream()
                .sorted(Comparator.comparing(Recipe::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    // Sort by cook time ascending for convenience-oriented browsing.
    public List<Recipe> sortByCookTime() {
        return currentRecommendations.stream()
                .sorted(Comparator.comparing(Recipe::getCookTime))
                .collect(Collectors.toList());
    }

    @Override
    // Clear recommendation session state at loop end.
    public void clearRecommendations() {
        currentRecommendations = new ArrayList<>();
    }
}
