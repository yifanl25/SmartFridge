package service;

import model.FoodItem;
import model.HealthGoal;
import model.Preference;
import model.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IRecommendationService} that scores recipes based on
 * ingredient coverage, health goal alignment, urgent ingredient usage, and cook time.
 * <p>
 * Scoring breakdown per recipe (max 100 points):
 * <ul>
 *   <li>Up to 60 points — required ingredient coverage (matched / total × 60)</li>
 *   <li>Up to 20 points — urgent ingredient usage bonus</li>
 *   <li>Up to 15 points — health goal alignment with recipe tags</li>
 *   <li>Up to 5 points  — convenience bonus based on cook time</li>
 * </ul>
 * The scored and sorted result list is cached per session and reused by
 * filter and sort methods until {@link #clearRecommendations()} is called.
 */
public class RecommendationService implements IRecommendationService {
    /**
     * Immutable recipe templates loaded from JSON.
     */
    private final List<Recipe> recipeTemplates;

    /**
     * Catalog used to resolve and normalize ingredient names for matching.
     */
    private final IFoodCatalog foodCatalog;

    /**
     * Cached result list from the most recent {@link #getRecommendations} call.
     */
    private List<Recipe> currentRecommendations;

    /**
     * Constructs the service with recipe templates and a food catalog.
     *
     * @param recipeTemplates immutable recipe templates loaded from JSON
     * @param foodCatalog     catalog for canonical ingredient name resolution
     */
    public RecommendationService(List<Recipe> recipeTemplates, IFoodCatalog foodCatalog) {
        this.recipeTemplates = new ArrayList<>(recipeTemplates);
        this.foodCatalog = foodCatalog;
        this.currentRecommendations = new ArrayList<>();
    }

    /**
     * Normalizes a string for comparison by trimming whitespace and converting to lowercase.
     *
     * @param s the string to normalize
     * @return normalized string, or empty string if input is null
     */
    private static String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Finds the inventory item whose canonical name matches the given ingredient name.
     * <p>
     * Both names are resolved through the food catalog before comparison
     * to handle aliases (e.g. "chicken" matching "chicken breast").
     *
     * @param ingredientName the ingredient name from the recipe
     * @param inventory      the user's current fridge inventory
     * @return the matching {@link FoodItem}, or {@code null} if not found
     */
    private FoodItem findMatchingInventoryItem(String ingredientName, List<FoodItem> inventory) {
        String ca = norm(foodCatalog.canonicalFoodName(ingredientName));
        for (FoodItem item : inventory) {
            String cb = norm(foodCatalog.canonicalFoodName(item.getName()));
            if (ca.equals(cb)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns a convenience bonus score based on the recipe's cook time.
     * <p>
     * Buckets:
     * <ul>
     *   <li>≤ 15 minutes → 5 points</li>
     *   <li>≤ 30 minutes → 3 points</li>
     *   <li>longer       → 0 points</li>
     * </ul>
     *
     * @param cookTime cook time in minutes
     * @return convenience bonus points
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
     * Returns bonus points based on alignment between the user's health goal
     * and the recipe's health tags.
     * <p>
     * Scoring:
     * <ul>
     *   <li>Exact tag match (e.g. MUSCLE_BUILDING + HIGH_PROTEIN) → 15 points</li>
     *   <li>Recipe tagged BALANCED as fallback → 8 points</li>
     *   <li>No match → 0 points</li>
     * </ul>
     *
     * @param preference the user's current preference, or {@code null} if unset
     * @param tags       the recipe's health tags
     * @return alignment bonus points
     */
    private static int preferenceAlignment(Preference preference, List<Recipe.HealthTag> tags) {
        if (preference == null) {
            return 0;
        }
        HealthGoal g = preference.getHealthGoal();
        boolean balanced = tags.contains(Recipe.HealthTag.BALANCED);
        int best = 0;
        switch (g) {
            case MUSCLE_BUILDING:
                if (tags.contains(Recipe.HealthTag.HIGH_PROTEIN)) {
                    best = 15;
                } else if (balanced) {
                    best = 8;
                }
                break;
            case FAT_LOSS:
                if (tags.contains(Recipe.HealthTag.LOW_CALORIE)) {
                    best = 15;
                } else if (balanced) {
                    best = 8;
                }
                break;
            case BLOOD_SUGAR_CARE:
                if (tags.contains(Recipe.HealthTag.BLOOD_SUGAR_FRIENDLY)) {
                    best = 15;
                } else if (balanced) {
                    best = 8;
                }
                break;
            default:
                break;
        }
        return best;
    }

    /**
     * Returns a bonus score based on how many required ingredients are urgent
     * (expiring soon) in the user's inventory.
     * <p>
     * Encourages using ingredients before they expire:
     * <ul>
     *   <li>0 urgent matches → 0 points</li>
     *   <li>1 urgent match   → 10 points</li>
     *   <li>2+ urgent matches → 20 points</li>
     * </ul>
     *
     * @param urgentMatchedRequiredCount number of required ingredients matched by urgent items
     * @return urgent usage bonus points
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
     * Scores a single recipe template against the user's inventory and preference.
     * <p>
     * Checks each required (non-optional) ingredient against the inventory,
     * calculates a coverage score, then adds urgency, preference alignment,
     * and convenience bonuses. The total is capped at 100.
     * Returns a new {@link Recipe} instance via {@link Recipe#withComputed} with
     * the score, available ingredient list, missing ingredient list, and urgent match count attached.
     *
     * @param template   the recipe template loaded from JSON
     * @param inventory  the user's current fridge inventory
     * @param preference the user's current health goal preference
     * @return a scored copy of the recipe with computed fields filled in
     */
    private Recipe scoreRecipe(Recipe template, List<FoodItem> inventory, Preference preference) {
        List<Recipe.Ingredient> required = template.getRequiredIngredients().stream()
                .filter(ri -> !ri.isOptional())
                .collect(Collectors.toList());

        int totalRequired = required.size();
        int matched = 0;
        int urgentMatched = 0;
        List<String> available = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (Recipe.Ingredient ri : required) {
            FoodItem hit = findMatchingInventoryItem(ri.getName(), inventory);
            if (hit != null) {
                matched++;
                if (hit.isUrgent()) {
                    urgentMatched++;
                }
                available.add(ri.getName());
            } else {
                missing.add(ri.getName());
            }
        }

        double coverage = 0.0;
        if (totalRequired > 0) {
            coverage = 60.0 * matched / totalRequired;
        }

        double score = coverage
                + urgentUsageScore(urgentMatched)
                + preferenceAlignment(preference, template.getHealthTags())
                + convenienceScore(template.getCookTime());

        if (score > 100.0) {
            score = 100.0;
        }

        return template.withComputed(score, available, missing, urgentMatched);
    }

    /**
     * Returns a comparator that sorts recipes by match score with multi-level tie-breaking.
     * <p>
     * Tie-break priority (in order):
     * <ol>
     *   <li>Highest match score</li>
     *   <li>Fewest missing ingredients</li>
     *   <li>Most urgent ingredients matched</li>
     *   <li>Fastest cook time</li>
     *   <li>Highest rating</li>
     *   <li>Title alphabetically (last resort)</li>
     * </ol>
     *
     * @return a multi-level comparator for recipe sorting
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
     * Scores all recipe templates against the current inventory and preference,
     * sorts the results using the tie-break comparator, caches them, and returns the list.
     *
     * @param inventory  the user's current fridge inventory, or {@code null} for empty
     * @param preference the user's current health goal preference
     * @return sorted list of scored recipes
     */
    @Override
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        List<Recipe> scored = new ArrayList<>();
        for (Recipe tmpl : recipeTemplates) {
            scored.add(scoreRecipe(tmpl, inventory == null ? List.of() : inventory, preference));
        }
        scored.sort(tieBreak());
        currentRecommendations = new ArrayList<>(scored);
        return new ArrayList<>(currentRecommendations);
    }

    /**
     * Filters the cached recommendation list by recipe category name.
     * <p>
     * Uses a case-insensitive partial match (e.g. "break" matches "Breakfast").
     *
     * @param categoryName the category name to filter by
     * @return filtered list of recipes matching the category
     */
    @Override
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        String needle = categoryName == null ? "" : categoryName.trim().toLowerCase(Locale.ROOT);
        return currentRecommendations.stream()
                .filter(r -> r.getRecipeCategory().getName().toLowerCase(Locale.ROOT).contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * Re-sorts the cached recommendation list by match score using the tie-break comparator.
     *
     * @return sorted list of recipes by match score
     */
    @Override
    public List<Recipe> sortByMatchScore() {
        return currentRecommendations.stream()
                .sorted(tieBreak())
                .collect(Collectors.toList());
    }

    /**
     * Re-sorts the cached recommendation list by cook time ascending,
     * with match score and other tie-breakers applied afterwards.
     *
     * @return sorted list of recipes by fastest cook time first
     */
    @Override
    public List<Recipe> sortByCookTime() {
        return currentRecommendations.stream()
                .sorted(Comparator.comparingInt(Recipe::getCookTime)
                        .thenComparing(Comparator.comparingDouble(Recipe::getMatchScore).reversed())
                        .thenComparingInt(r -> r.getMissingIngredients().size())
                        .thenComparing(Comparator.comparingInt(Recipe::getUrgentMatchedCount).reversed())
                        .thenComparing(Comparator.comparingDouble(Recipe::getRating).reversed())
                        .thenComparing(Recipe::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Clears the cached recommendation list.
     * <p>
     * Call this when the user resets their session or logs out.
     */
    @Override
    public void clearRecommendations() {
        currentRecommendations = new ArrayList<>();
    }
}
