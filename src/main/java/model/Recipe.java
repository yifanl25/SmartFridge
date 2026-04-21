package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe aggregate: static fields from JSON plus runtime-computed match score and ingredient availability.
 * {@link HealthTag} and {@link Ingredient} are static nested types to keep recipe vocabulary in one place.
 * <p>
 */
public class Recipe {

    /**
     * Tags used for preference-alignment scoring (not the same as {@link HealthGoal} on {@link Preference}).
     * <p>
     */
    public enum HealthTag {
        /** High protein positioning. */
        HIGH_PROTEIN,
        /** Low calorie positioning. */
        LOW_CALORIE,
        /** Blood-sugar friendly positioning. */
        BLOOD_SUGAR_FRIENDLY,
        /** Neutral / balanced fallback alignment. */
        BALANCED
    }

    /**
     * One ingredient line on a recipe; lines with {@code optional==true} are excluded from main coverage score.
     * <p>
     */
    public static final class Ingredient {
        private final String name;
        private final String quantityText;
        private final boolean optional;

        /**
         * @param name         ingredient name
         * @param quantityText human-readable quantity
         * @param optional     whether excluded from required coverage
         */
        public Ingredient(String name, String quantityText, boolean optional) {
            this.name = name;
            this.quantityText = quantityText;
            this.optional = optional;
        }

        /** Returns ingredient name. */
        public String getName() {
            return name;
        }

        /** Returns quantity text. */
        public String getQuantityText() {
            return quantityText;
        }

        /** Returns whether this line is optional for scoring. */
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
    /** PRD tie-break: required ingredients matched by urgent inventory items. */
    private final int urgentMatchedCount;

    /**
     * Full constructor including computed fields (used internally and by {@link #withComputed}).
     * <p>
     */
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
     * Factory for JSON-loaded templates before scoring (match score 0, empty availability lists).
     * <p>
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
                List.of(),
                List.of(),
                0);
    }

    /**
     * Returns a copy with computed match fields (immutable outer recipe).
     * <p>
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
                urgentMatchedCount);
    }

    /** Returns recipe id. */
    public String getId() {
        return id;
    }

    /** Returns title. */
    public String getTitle() {
        return title;
    }

    /** Returns recipe category (not food category). */
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /** Returns a defensive copy of health tags. */
    public List<HealthTag> getHealthTags() {
        return new ArrayList<>(healthTags);
    }

    /** Returns a defensive copy of required ingredient lines. */
    public List<Ingredient> getRequiredIngredients() {
        return new ArrayList<>(requiredIngredients);
    }

    /** Returns a defensive copy of optional ingredient lines. */
    public List<Ingredient> getOptionalIngredients() {
        return new ArrayList<>(optionalIngredients);
    }

    /** Returns computed match score (0 before scoring). */
    public double getMatchScore() {
        return matchScore;
    }

    /** Returns star-style rating from JSON. */
    public double getRating() {
        return rating;
    }

    /** Returns cook time in minutes. */
    public int getCookTime() {
        return cookTime;
    }

    /** Returns calories estimate. */
    public int getCalories() {
        return calories;
    }

    /** Returns description text. */
    public String getDescription() {
        return description;
    }

    /** Returns matched required ingredient names (runtime). */
    public List<String> getAvailableIngredients() {
        return new ArrayList<>(availableIngredients);
    }

    /** Returns missing required ingredient names (runtime). */
    public List<String> getMissingIngredients() {
        return new ArrayList<>(missingIngredients);
    }

    /** Returns urgent-match count for tie-break. */
    public int getUrgentMatchedCount() {
        return urgentMatchedCount;
    }
}
