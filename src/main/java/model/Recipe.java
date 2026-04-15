package model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    // Stable recipe id from static recipes.json.
    private final String id;
    // Display title used by recommendation cards and detail page.
    private final String title;
    // PRD: recipe grouping taxonomy (not FoodCategory).
    private final RecipeCategory recipeCategory;
    // Final computed score [0,100] produced by RecommendationService formula.
    private final double matchScore;
    // User-facing rating used in deterministic tie-break after cookTime.
    private final double rating;
    // Minutes used by convenience score and tie-break rule.
    private final int cookTime;
    // Display nutrition value.
    private final int calories;
    // Text description shown on detail view.
    private final String description;
    // Computed matched ingredient names for current inventory session.
    private final List<String> availableIngredients;
    // Computed missing required ingredient names; source for Grocery list.
    private final List<String> missingIngredients;

    // Immutable recipe view model; required/optional ingredient matching is handled in service layer.
    public Recipe(
            String id,
            String title,
            RecipeCategory recipeCategory,
            double matchScore,
            double rating,
            int cookTime,
            int calories,
            String description,
            List<String> availableIngredients,
            List<String> missingIngredients) {
        this.id = id;
        this.title = title;
        this.recipeCategory = recipeCategory;
        this.matchScore = matchScore;
        this.rating = rating;
        this.cookTime = cookTime;
        this.calories = calories;
        this.description = description;
        this.availableIngredients = new ArrayList<>(availableIngredients);
        this.missingIngredients = new ArrayList<>(missingIngredients);
    }

    // Returns static recipe id.
    public String getId() { return id; }
    // Returns recipe title for card/detail rendering.
    public String getTitle() { return title; }
    // Returns RecipeCategory for recommendation filtering.
    public RecipeCategory getRecipeCategory() { return recipeCategory; }
    // Returns already-computed match score (service computes from requiredIngredients only).
    public double getMatchScore() { return matchScore; }
    // Returns recipe rating used by tie-break rule #4.
    public double getRating() { return rating; }
    // Returns cook duration for convenience score and tie-break rule #3.
    public int getCookTime() { return cookTime; }
    // Returns calories for display purposes.
    public int getCalories() { return calories; }
    // Returns long-form recipe description.
    public String getDescription() { return description; }
    // Defensive copy to prevent external mutation of match-result data.
    public List<String> getAvailableIngredients() { return new ArrayList<>(availableIngredients); }
    // Defensive copy to prevent external mutation of missing ingredient set.
    public List<String> getMissingIngredients() { return new ArrayList<>(missingIngredients); }
}
