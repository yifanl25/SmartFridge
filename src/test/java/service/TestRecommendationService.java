package service;

import model.HealthGoal;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：Recommendation logic, sorting, and filtering.(Maps to: {@link RecommendationService}，including PRD scoring and tie-break).
 */
public class TestRecommendationService {
    private RecommendationService recommendationService;
    private List<Recipe> recipes;

    @BeforeEach
    void setUp() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        recipes = List.of(
                Recipe.loaded(
                        "1",
                        "A",
                        cat,
                        List.of(Recipe.HealthTag.BALANCED),
                        List.of(new Recipe.Ingredient("Egg", "1", false)),
                        List.of(),
                        4.5,
                        10,
                        200,
                        "d"),
                Recipe.loaded(
                        "2",
                        "B",
                        cat,
                        List.of(Recipe.HealthTag.BALANCED),
                        List.of(new Recipe.Ingredient("Milk", "1", false)),
                        List.of(),
                        4.1,
                        20,
                        300,
                        "d"));
        recommendationService = new RecommendationService(recipes, new FoodCatalog(List.of()));
    }

    /**
     * Test: Generates recommendation list based on inventory and user preference.
     * Verification: Returns all template recipes (2 in this fixture).
     * <p>
     * Maps to: {@link RecommendationService#getRecommendations(java.util.List, model.Preference)}
     */
    @Test
    void testGetRecommendationsUsesInventoryAndPreference() {
        assertEquals(2, recommendationService.getRecommendations(List.of(), new Preference("p", HealthGoal.FAT_LOSS)).size());
    }

    /**
     * Test: Filters current recommendations by recipe category (substring match).
     * Verification: 'Quick' matches both recipes in this fixture.
     * <p>
     * Maps to: {@link RecommendationService#filterByRecipeCategory(String)}
     */
    @Test
    void testFilterByRecipeCategoryReturnsMatchingRecipes() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals(2, recommendationService.filterByRecipeCategory("quick").size());
    }

    /**
     * Test: Sorts current recommendations by match score in descending order.
     * Verification: Higher-scored recipe "A" appears first.
     * <p>
     * Maps to: {@link RecommendationService#sortByMatchScore()}
     */
    @Test
    void testSortByMatchScoreOrdersDescending() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals("A", recommendationService.sortByMatchScore().get(0).getTitle());
    }

    /**
     * Test: Sorts current recommendations by cooking time in ascending order.
     * Verification: 10-minute recipe "A" appears before 20-minute recipe "B".
     * <p>
     * Maps to: {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testSortByCookTimeOrdersAscending() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals("A", recommendationService.sortByCookTime().get(0).getTitle());
    }

    /**
     * Test: Clears current recommendation cache (checkout/session reset flow).
     * Verification: After clearing, sorting returns an empty list.
     * <p>
     * Maps to: {@link RecommendationService#clearRecommendations()}, {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testClearRecommendationsRemovesCurrentResults() {
        recommendationService.getRecommendations(List.of(), null);
        recommendationService.clearRecommendations();
        assertEquals(0, recommendationService.sortByCookTime().size());
    }
}
