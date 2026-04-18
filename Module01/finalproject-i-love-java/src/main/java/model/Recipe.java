package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one recipe template plus computed recommendation fields.
 */
public class Recipe {

    /**
     * Health tags used for recommendation scoring.
     */
    public enum HealthTag {
        HIGH_PROTEIN,
        LOW_CALORIE,
        BLOOD_SUGAR_FRIENDLY,
        BALANCED
    }

    /**
     * Represents one ingredient line in a recipe.
     */
    public static final class Ingredient {
        private final String name;
        private final String quantityText;
        private final boolean optional;

        public Ingredient(String name, String quantityText, boolean optional) {
            this.name = name;
            this.quantityText = quantityText;
            this.optional = optional;
        }

        public String getName() {
            return name;
        }

        public String getQuantityText() {
            return quantityText;
        }

        public boolean isOptional() {
            return optional;
        }
    }

    private final String id;
    private final String title;
    private final RecipeCategory recipeCategory;
    private final List<HealthTag> healthTags;
    private final List<Ingredient> requiredIngredients;
    private final List<Ingredient> optionalIngredients;
    private final double matchScore;
    private final double rating;
    private final int cookTime;
    private final int calories;
    private final String description;
    private final List<String> availableIngredients;
    private final List<String> missingIngredients;
    private final int urgentMatchedCount;

    public Recipe(
            String id,
            String title,
            RecipeCategory recipeCategory,
            List<HealthTag> healthTags,
            List<Ingredient> requiredIngredients,
            List<Ingredient> optionalIngredients,
            double matchScore,
            double rating,
            int cookTime,
            int calories,
            String description,
            List<String> availableIngredients,
            List<String> missingIngredients,
            int urgentMatchedCount) {
        this.id = id;
        this.title = title;
        this.recipeCategory = recipeCategory;
        this.healthTags = new ArrayList<>(healthTags);
        this.requiredIngredients = new ArrayList<>(requiredIngredients);
        this.optionalIngredients = new ArrayList<>(optionalIngredients);
        this.matchScore = matchScore;
        this.rating = rating;
        this.cookTime = cookTime;
        this.calories = calories;
        this.description = description;
        this.availableIngredients = new ArrayList<>(availableIngredients);
        this.missingIngredients = new ArrayList<>(missingIngredients);
        this.urgentMatchedCount = urgentMatchedCount;
    }

    /**
     * Creates one recipe loaded from JSON before runtime scoring.
     */
    public static Recipe loaded(
            String id,
            String title,
            RecipeCategory recipeCategory,
            List<HealthTag> healthTags,
            List<Ingredient> requiredIngredients,
            List<Ingredient> optionalIngredients,
            double rating,
            int cookTime,
            int calories,
            String description) {
        return new Recipe(
                id,
                title,
                recipeCategory,
                healthTags,
                requiredIngredients,
                optionalIngredients,
                0.0,
                rating,
                cookTime,
                calories,
                description,
                new ArrayList<>(),
                new ArrayList<>(),
                0
        );
    }

    /**
     * Returns a copy with computed recommendation fields.
     */
    public Recipe withComputed(
            double matchScore,
            List<String> availableIngredients,
            List<String> missingIngredients,
            int urgentMatchedCount) {
        return new Recipe(
                id,
                title,
                recipeCategory,
                healthTags,
                requiredIngredients,
                optionalIngredients,
                matchScore,
                rating,
                cookTime,
                calories,
                description,
                availableIngredients,
                missingIngredients,
                urgentMatchedCount
        );
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    public List<HealthTag> getHealthTags() {
        return new ArrayList<>(healthTags);
    }

    public List<Ingredient> getRequiredIngredients() {
        return new ArrayList<>(requiredIngredients);
    }

    public List<Ingredient> getOptionalIngredients() {
        return new ArrayList<>(optionalIngredients);
    }

    public double getMatchScore() {
        return matchScore;
    }

    public double getRating() {
        return rating;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCalories() {
        return calories;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAvailableIngredients() {
        return new ArrayList<>(availableIngredients);
    }

    public List<String> getMissingIngredients() {
        return new ArrayList<>(missingIngredients);
    }

    public int getUrgentMatchedCount() {
        return urgentMatchedCount;
    }
}
