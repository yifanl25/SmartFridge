import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters recipes based on the user's health goal and nutrition thresholds,
 * then sorts them by the selected sort option.
 * <p>
 * Nutrition thresholds per serving (based on dietary guidelines):
 * <p>
 * Muscle Building: protein_g >= 25.
 * Fat Loss: calories <= 300.
 * Blood Sugar Care: fiber_g >= 4.
 */
public class RecipeServiceImpl implements IRecipeService {

    /**
     * Minimum protein per serving for muscle building recipes.
     */
    private static final int MUSCLE_BUILDING_MIN_PROTEIN_G = 25;

    /**
     * Maximum calories per serving for fat loss recipes.
     */
    private static final int FAT_LOSS_MAX_CALORIES = 300;

    /**
     * Minimum fiber per serving for blood sugar care recipes.
     */
    private static final int BLOOD_SUGAR_MIN_FIBER_G = 4;

    private final IFridgeRepository repository;

    /**
     * Constructs a RecipeServiceImpl with the given repository.
     *
     * @param repository the data source for recipes and inventory
     */
    public RecipeServiceImpl(IFridgeRepository repository) {
        this.repository = repository;
    }

    /**
     * Filters all recipes to only those that meet the nutrition threshold
     * for the user's selected goal, then sorts by the given sort option.
     *
     */
    @Override
    public List<Recipe> getRecommended(UserPreference preference, String sortOption) {
        List<Recipe> allRecipes = repository.findAllRecipes();
        String goal = preference.getGoals().isEmpty() ? null : preference.getGoals().get(0);

        List<Recipe> filtered = filter(allRecipes, goal);
        return sort(filtered, goal, sortOption);
    }

    @Override
    public List<String> getMissing(Recipe recipe) {
        List<String> inventoryNames = repository.findAll()
                .stream()
                .map(Ingredient::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return recipe.getRequiredIngredients()
                .stream()
                .filter(i -> !inventoryNames.contains(i.toLowerCase()))
                .collect(Collectors.toList());
    }


    /**
     * Filters recipes to only those that meet the nutrition threshold
     * for the given health goal.
     *
     * @param recipes all available recipes
     * @param goal    the user's selected health goal
     * @return filtered list of matching recipes
     */
    private List<Recipe> filter(List<Recipe> recipes, String goal) {
        if (goal == null) return recipes;

        return recipes.stream()
                .filter(recipe -> meetsThreshold(recipe, goal))
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the recipe meets the nutrition threshold for the given goal.
     *
     * @param recipe the recipe to evaluate
     * @param goal   the user's health goal
     * @return true if the recipe qualifies for the goal
     */
    private boolean meetsThreshold(Recipe recipe, String goal) {
        switch (goal) {
            case UserPreference.GOAL_MUSCLE_BUILDING:
                return recipe.getProteinG() >= MUSCLE_BUILDING_MIN_PROTEIN_G;
            case UserPreference.GOAL_FAT_LOSS:
                return recipe.getCalories() <= FAT_LOSS_MAX_CALORIES;
            case UserPreference.GOAL_BLOOD_SUGAR_CARE:
                return recipe.getFiberG() >= BLOOD_SUGAR_MIN_FIBER_G;
            default:
                return true;
        }
    }

    /**
     * Sorts the filtered recipes by the selected sort option.
     *
     * @param recipes    filtered list of recipes
     * @param goal       the user's health goal (used for high match sorting)
     * @param sortOption one of SORT_ALL, SORT_HIGH_MATCH, SORT_FASTEST
     * @return sorted list of recipes
     */
    private List<Recipe> sort(List<Recipe> recipes, String goal, String sortOption) {
        switch (sortOption) {
            case SORT_HIGH_MATCH:
                return sortByHighMatch(recipes, goal);
            case SORT_FASTEST:
                return recipes.stream()
                        .sorted(Comparator.comparingInt(Recipe::getCookTimeMin))
                        .collect(Collectors.toList());
            case SORT_ALL:
            default:
                return recipes;
        }
    }

    /**
     * Sorts recipes by the most relevant nutrition value for the given goal.
     * <ul>
     *   <li>Muscle Building → highest protein first</li>
     *   <li>Fat Loss → lowest calories first</li>
     *   <li>Blood Sugar Care → highest fiber first</li>
     * </ul>
     *
     * @param recipes filtered list of recipes
     * @param goal    the user's health goal
     * @return sorted list of recipes
     */
    private List<Recipe> sortByHighMatch(List<Recipe> recipes, String goal) {
        if (goal == null) return recipes;

        switch (goal) {
            case UserPreference.GOAL_MUSCLE_BUILDING:
                return recipes.stream()
                        .sorted(Comparator.comparingInt(Recipe::getProteinG).reversed())
                        .collect(Collectors.toList());
            case UserPreference.GOAL_FAT_LOSS:
                return recipes.stream()
                        .sorted(Comparator.comparingInt(Recipe::getCalories))
                        .collect(Collectors.toList());
            case UserPreference.GOAL_BLOOD_SUGAR_CARE:
                return recipes.stream()
                        .sorted(Comparator.comparingInt(Recipe::getFiberG).reversed())
                        .collect(Collectors.toList());
            default:
                return recipes;
        }
    }
}