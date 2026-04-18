package service;

import model.FoodItem;
import model.HealthGoal;
import model.Preference;
import model.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Scores and sorts recipe recommendations for the current session.
 */
public class RecommendationService implements IRecommendationService {

    private final List<Recipe> recipeTemplates;
    private final IFoodCatalog foodCatalog;
    private List<Recipe> currentRecommendations;

    public RecommendationService(List<Recipe> recipeTemplates, IFoodCatalog foodCatalog) {
        this.recipeTemplates = new ArrayList<>(recipeTemplates);
        this.foodCatalog = foodCatalog;
        this.currentRecommendations = new ArrayList<>();
    }

    /**
     * Normalizes text for comparison.
     */
    private static String norm(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Finds the inventory item that matches the ingredient name after catalog normalization.
     */
    private FoodItem findMatchingInventoryItem(String ingredientName, List<FoodItem> inventory) {
        String targetName = norm(foodCatalog.canonicalFoodName(ingredientName));

        for (FoodItem item : inventory) {
            String itemName = norm(foodCatalog.canonicalFoodName(item.getName()));
            if (targetName.equals(itemName)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns convenience points based on cook time.
     */
    private static int convenienceScore(int cookTime) {
        if (cookTime <= 15) {
            return 5;
        }
        if (cookTime <= 30) {
            return 3;
        }
        return 0;
    }

    /**
     * Returns score based on the selected health goal and recipe tags.
     */
    private static int preferenceAlignment(Preference preference, List<Recipe.HealthTag> tags) {
        if (preference == null) {
            return 0;
        }

        HealthGoal goal = preference.getHealthGoal();
        boolean balanced = tags.contains(Recipe.HealthTag.BALANCED);

        switch (goal) {
            case MUSCLE_BUILDING:
                if (tags.contains(Recipe.HealthTag.HIGH_PROTEIN)) {
                    return 15;
                }
                if (balanced) {
                    return 8;
                }
                return 0;

            case FAT_LOSS:
                if (tags.contains(Recipe.HealthTag.LOW_CALORIE)) {
                    return 15;
                }
                if (balanced) {
                    return 8;
                }
                return 0;

            case BLOOD_SUGAR_CARE:
                if (tags.contains(Recipe.HealthTag.BLOOD_SUGAR_FRIENDLY)) {
                    return 15;
                }
                if (balanced) {
                    return 8;
                }
                return 0;

            default:
                return 0;
        }
    }

    /**
     * Returns bonus points when urgent ingredients are used.
     */
    private static int urgentUsageScore(int urgentMatchedRequiredCount) {
        if (urgentMatchedRequiredCount <= 0) {
            return 0;
        }
        if (urgentMatchedRequiredCount == 1) {
            return 10;
        }
        return 20;
    }

    /**
     * Scores one recipe template and returns a computed recipe result.
     */
    private Recipe scoreRecipe(Recipe template, List<FoodItem> inventory, Preference preference) {
        List<Recipe.Ingredient> requiredIngredients = new ArrayList<>();

        for (Recipe.Ingredient ingredient : template.getRequiredIngredients()) {
            if (!ingredient.isOptional()) {
                requiredIngredients.add(ingredient);
            }
        }

        int totalRequired = requiredIngredients.size();
        int matchedRequiredCount = 0;
        int urgentMatchedRequiredCount = 0;

        List<String> availableIngredients = new ArrayList<>();
        List<String> missingIngredients = new ArrayList<>();

        for (Recipe.Ingredient ingredient : requiredIngredients) {
            FoodItem match = findMatchingInventoryItem(ingredient.getName(), inventory);

            if (match != null) {
                matchedRequiredCount++;
                availableIngredients.add(ingredient.getName());

                if (match.isUrgent()) {
                    urgentMatchedRequiredCount++;
                }
            } else {
                missingIngredients.add(ingredient.getName());
            }
        }

        double coverageScore = 0.0;
        if (totalRequired > 0) {
            coverageScore = 60.0 * matchedRequiredCount / totalRequired;
        }

        double finalScore = coverageScore
                + urgentUsageScore(urgentMatchedRequiredCount)
                + preferenceAlignment(preference, template.getHealthTags())
                + convenienceScore(template.getCookTime());

        if (finalScore > 100.0) {
            finalScore = 100.0;
        }

        return template.withComputed(
                finalScore,
                availableIngredients,
                missingIngredients,
                urgentMatchedRequiredCount
        );
    }

    /**
     * Comparator used for the main recommendation ranking.
     */
    private static Comparator<Recipe> tieBreak() {
        return Comparator.comparingDouble(Recipe::getMatchScore).reversed()
                .thenComparingInt(r -> r.getMissingIngredients().size())
                .thenComparing(Comparator.comparingInt(Recipe::getUrgentMatchedCount).reversed())
                .thenComparingInt(Recipe::getCookTime)
                .thenComparing(Comparator.comparingDouble(Recipe::getRating).reversed())
                .thenComparing(Recipe::getTitle, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Returns the recommendation list for the current inventory and preference.
     */
    @Override
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        List<FoodItem> safeInventory;
        if (inventory == null) {
            safeInventory = new ArrayList<>();
        } else {
            safeInventory = inventory;
        }

        List<Recipe> scoredRecipes = new ArrayList<>();

        for (Recipe template : recipeTemplates) {
            scoredRecipes.add(scoreRecipe(template, safeInventory, preference));
        }

        scoredRecipes.sort(tieBreak());
        currentRecommendations = new ArrayList<>(scoredRecipes);

        return new ArrayList<>(currentRecommendations);
    }

    /**
     * Filters the current recommendations by recipe category.
     */
    @Override
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return new ArrayList<>(currentRecommendations);
        }

        String target = categoryName.trim().toLowerCase(Locale.ROOT);
        List<Recipe> result = new ArrayList<>();

        for (Recipe recipe : currentRecommendations) {
            String recipeCategory = recipe.getRecipeCategory().getName().toLowerCase(Locale.ROOT);
            if (recipeCategory.contains(target)) {
                result.add(recipe);
            }
        }

        return result;
    }

    /**
     * Sorts the current recommendations by match score.
     */
    @Override
    public List<Recipe> sortByMatchScore() {
        List<Recipe> result = new ArrayList<>(currentRecommendations);
        result.sort(tieBreak());
        return result;
    }

    /**
     * Sorts the current recommendations by cook time.
     */
    @Override
    public List<Recipe> sortByCookTime() {
        List<Recipe> result = new ArrayList<>(currentRecommendations);

        result.sort(
                Comparator.comparingInt(Recipe::getCookTime)
                        .thenComparing(Comparator.comparingDouble(Recipe::getMatchScore).reversed())
                        .thenComparingInt(r -> r.getMissingIngredients().size())
                        .thenComparing(Comparator.comparingInt(Recipe::getUrgentMatchedCount).reversed())
                        .thenComparing(Comparator.comparingDouble(Recipe::getRating).reversed())
                        .thenComparing(Recipe::getTitle, String.CASE_INSENSITIVE_ORDER)
        );

        return result;
    }

    /**
     * Clears the cached recommendations for the current session.
     */
    @Override
    public void clearRecommendations() {
        currentRecommendations = new ArrayList<>();
    }
}