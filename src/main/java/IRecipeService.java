import java.util.List;

/**
 * Service interface for recipe recommendation logic.
 */
public interface IRecipeService {

    /** Return all suitable recipes with no ordering. */
    String SORT_ALL = "all";

    /** Sort by best nutritional fit for the user's goal. */
    String SORT_HIGH_MATCH = "high_match";

    /** Sort by fastest cook time. */
    String SORT_FASTEST = "fastest";

    /**
     * Returns a filtered list of recipes that match the user's health goal,
     * sorted by the given sort option.
     *
     * @param preference the user's selected preference
     * @param sortOption one of sort options: SORT_ALL, SORT_HIGH_MATCH, SORT_FASTEST
     * @return filtered and sorted list of recommended recipes
     */
    List<Recipe> getRecommended(UserPreference preference, String sortOption);

    /**
     * Returns the list of ingredients required for a recipe that are
     * not currently in the user's inventory.
     *
     * @param recipe the selected recipe
     * @return list of missing ingredient names
     */
    List<String> getMissing(Recipe recipe);
}