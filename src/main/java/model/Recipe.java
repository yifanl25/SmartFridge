package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a recipe with both static data loaded from JSON and runtime-computed fields.
 * <p>
 * This class is immutable — all fields are {@code final}. It follows a two-step lifecycle:
 * <ol>
 *   <li>{@link #loaded} — creates a template from JSON with no computed fields</li>
 *   <li>{@link #withComputed} — returns a new copy with match score and ingredient availability filled in</li>
 * </ol>
 * {@link HealthTag} and {@link Ingredient} are nested types to keep all recipe-related
 * vocabulary in one place.
 * </p>
 */
public class Recipe {

    /**
     * Health positioning tags used for preference-alignment scoring.
     * <p>
     * These are recipe-side labels and are not the same as the user-side {@link HealthGoal}
     * on {@link Preference}. The recommendation service maps each {@link HealthGoal}
     * to its corresponding tag to calculate alignment bonus points.
     * </p>
     */
    public enum HealthTag {
        /**
         * High protein positioning.
         */
        HIGH_PROTEIN,
        /**
         * Low calorie positioning.
         */
        LOW_CALORIE,
        /**
         * Blood-sugar friendly positioning.
         */
        BLOOD_SUGAR_FRIENDLY,
        /**
         * Neutral / balanced fallback alignment.
         */
        BALANCED
    }

    /**
     * Represents a single ingredient line on a recipe.
     * <p>
     * Lines where {@code optional} is {@code true} are excluded from the required
     * ingredient coverage score, so missing optional ingredients do not penalize the recipe.
     * </p>
     */
    public static final class Ingredient {
        private final String name;
        private final String quantityText;
        private final boolean optional;

        /**
         * Constructs an ingredient line.
         *
         * @param name         the ingredient name
         * @param quantityText human-readable quantity (e.g. "2 cloves", "1 cup")
         * @param optional     {@code true} if this ingredient is excluded from coverage scoring
         */
        public Ingredient(String name, String quantityText, boolean optional) {
            this.name = name;
            this.quantityText = quantityText;
            this.optional = optional;
        }

        /**
         * Returns the ingredient name.
         *
         * @return ingredient name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the human-readable quantity text.
         *
         * @return quantity text (e.g. "2 cloves")
         */
        public String getQuantityText() {
            return quantityText;
        }

        /**
         * Returns whether this ingredient is optional for scoring purposes.
         *
         * @return {@code true} if excluded from required coverage score
         */
        public boolean isOptional() {
            return optional;
        }
    }

    /**
     * Unique recipe identifier from JSON.
     */
    private final String id;

    /**
     * Display title of the recipe.
     */
    private final String title;

    /**
     * The recipe's category (e.g. Breakfast, Dinner) — not the same as food category.
     */
    private final RecipeCategory recipeCategory;

    /**
     * Health tags used for preference alignment scoring.
     */
    private final List<HealthTag> healthTags;

    /**
     * Required ingredient lines — used for coverage scoring.
     */
    private final List<Ingredient> requiredIngredients;

    /**
     * Optional ingredient lines — excluded from coverage scoring.
     */
    private final List<Ingredient> optionalIngredients;

    /**
     * Computed match score (0.0 before scoring, up to 100.0 after).
     */
    private final double matchScore;

    /**
     * Star-style rating from JSON data.
     */
    private final double rating;

    /**
     * Total cook time in minutes.
     */
    private final int cookTime;

    /**
     * Estimated calorie count per serving.
     */
    private final int calories;

    /**
     * Short description of the recipe.
     */
    private final String description;

    /**
     * Names of required ingredients found in the user's inventory (computed at runtime).
     */
    private final List<String> availableIngredients;

    /**
     * Names of required ingredients not found in the user's inventory (computed at runtime).
     */
    private final List<String> missingIngredients;

    /**
     * Number of required ingredients matched by urgent (expiring soon) inventory items — used for tie-breaking.
     */
    private final int urgentMatchedCount;

    /**
     * Full constructor used internally and by {@link #withComputed}.
     * <p>
     * Prefer using {@link #loaded} to create recipes from JSON,
     * and {@link #withComputed} to attach scored results.
     * </p>
     *
     * @param id                   recipe ID
     * @param title                recipe title
     * @param recipeCategory       recipe category
     * @param healthTags           health positioning tags
     * @param requiredIngredients  required ingredient lines
     * @param optionalIngredients  optional ingredient lines
     * @param matchScore           computed match score (0.0 to 100.0)
     * @param rating               star rating from JSON
     * @param cookTime             cook time in minutes
     * @param calories             estimated calories per serving
     * @param description          recipe description
     * @param availableIngredients names of ingredients found in inventory
     * @param missingIngredients   names of ingredients not found in inventory
     * @param urgentMatchedCount   number of required ingredients matched by urgent items
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
     * Factory method for creating a recipe template loaded from JSON.
     * <p>
     * The computed fields ({@code matchScore}, {@code availableIngredients},
     * {@code missingIngredients}, {@code urgentMatchedCount}) are set to their
     * empty defaults. Call {@link #withComputed} to attach scored results later.
     * </p>
     *
     * @param id                  recipe ID
     * @param title               recipe title
     * @param recipeCategory      recipe category
     * @param healthTags          health positioning tags
     * @param requiredIngredients required ingredient lines
     * @param optionalIngredients optional ingredient lines
     * @param rating              star rating from JSON
     * @param cookTime            cook time in minutes
     * @param calories            estimated calories per serving
     * @param description         recipe description
     * @return a recipe template with no computed fields
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
     * Returns a new immutable copy of this recipe with the computed fields attached.
     * <p>
     * All static fields (id, title, category, tags, ingredients, rating, cookTime,
     * calories, description) are copied from the original. Only the scoring-related
     * fields are replaced with the provided values.
     * </p>
     *
     * @param matchScore           the computed match score (0.0 to 100.0)
     * @param availableIngredients names of required ingredients found in inventory
     * @param missingIngredients   names of required ingredients not found in inventory
     * @param urgentMatchedCount   number of required ingredients matched by urgent items
     * @return a new Recipe with computed fields filled in
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

    /**
     * Returns the recipe ID.
     *
     * @return recipe ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the recipe title.
     *
     * @return recipe title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the recipe category (e.g. Breakfast, Dinner).
     * Note: this is not the same as a food ingredient category.
     *
     * @return recipe category
     */
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * Returns a defensive copy of the recipe's health tags.
     *
     * @return list of health tags
     */
    public List<HealthTag> getHealthTags() {
        return new ArrayList<>(healthTags);
    }

    /**
     * Returns a defensive copy of the required ingredient lines.
     * These are used for coverage scoring and grocery list generation.
     *
     * @return list of required ingredients
     */
    public List<Ingredient> getRequiredIngredients() {
        return new ArrayList<>(requiredIngredients);
    }

    /**
     * Returns a defensive copy of the optional ingredient lines.
     * These are excluded from coverage scoring.
     *
     * @return list of optional ingredients
     */
    public List<Ingredient> getOptionalIngredients() {
        return new ArrayList<>(optionalIngredients);
    }

    /**
     * Returns the computed match score.
     * Returns 0.0 if {@link #withComputed} has not been called yet.
     *
     * @return match score between 0.0 and 100.0
     */
    public double getMatchScore() {
        return matchScore;
    }

    /**
     * Returns the star-style rating from JSON.
     *
     * @return rating value
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returns the total cook time in minutes.
     *
     * @return cook time in minutes
     */
    public int getCookTime() {
        return cookTime;
    }

    /**
     * Returns the estimated calorie count per serving.
     *
     * @return calories per serving
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Returns the recipe description text.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the names of required ingredients found in the user's inventory.
     * Returns an empty list before {@link #withComputed} is called.
     *
     * @return list of available ingredient names
     */
    public List<String> getAvailableIngredients() {
        return new ArrayList<>(availableIngredients);
    }

    /**
     * Returns the names of required ingredients not found in the user's inventory.
     * Returns an empty list before {@link #withComputed} is called.
     *
     * @return list of missing ingredient names
     */
    public List<String> getMissingIngredients() {
        return new ArrayList<>(missingIngredients);
    }

    /**
     * Returns the number of required ingredients matched by urgent (expiring soon) inventory items.
     * Used as a tie-breaker in recommendation sorting.
     *
     * @return urgent match count
     */
    public int getUrgentMatchedCount() {
        return urgentMatchedCount;
    }
}
