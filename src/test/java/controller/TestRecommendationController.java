package controller;

import model.HealthGoal;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;
import service.FoodCatalog;
import service.IRecommendationService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD：RecommendationController tests.（Maps to: {@link RecommendationController} → {@link IRecommendationService}）。
 */
public class TestRecommendationController {
    private RecommendationController recommendationController;
    private IRecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        recommendationService = new RecommendationService(
                List.of(Recipe.loaded(
                        "1",
                        "A",
                        cat,
                        List.of(Recipe.HealthTag.BALANCED),
                        List.of(new Recipe.Ingredient("Egg", "1", false)),
                        List.of(),
                        4.5,
                        10,
                        200,
                        "d")),
                new FoodCatalog(List.of()));
        recommendationController = new RecommendationController(recommendationService);
    }

    /**
     * Tests filterByRecipeCategory() returns empty list for non-matching category.
     */
    @Test
    void testFilterByRecipeCategoryReturnsEmptyForNonMatchingCategory() {
        recommendationController.getRecommendations(List.of(), null);
        List<Recipe> result = recommendationController.filterByRecipeCategory("nonexistent");
        assertEquals(0, result.size());
    }

    /**
     * Tests sortByMatchScore() with a preference set returns ordered list.
     */
    @Test
    void testSortByMatchScoreWithPreferenceReturnsNonEmptyList() {
        Preference pref = new Preference("p1", HealthGoal.FAT_LOSS);
        recommendationController.getRecommendations(List.of(), pref);
        List<Recipe> result = recommendationController.sortByMatchScore();
        assertFalse(result.isEmpty());
    }


    /**
     * Tests getRecommendationById() and returns null when id does not exist.
     */
    @Test
    void testGetRecommendationByIdReturnsNullForUnknownId() {
        Recipe result = recommendationController.getRecommendationById(List.of(), null, "999");
        assertNull(result);
    }

    /**
     * Test: GetRecommendations by recommendation and inventory, delegates to service and returns expected result size.
     * <p>
     * Maps to: {@link RecommendationController#getRecommendations(java.util.List, model.Preference)} → {@link RecommendationService#getRecommendations(java.util.List, model.Preference)}
     */
    @Test
    void testGetRecommendationsDelegatesToRecommendationService() {
        assertEquals(1, recommendationController.getRecommendations(List.of(), null).size());
    }

    /**
     * Test：Filter recommendations ByRecipeCategory({@link RecipeCategory}).
     * Verification: after refreshing recommendations, filtered result size is correct.
     * <p>
     * Maps to: {@link RecommendationController#filterByRecipeCategory(String)} → {@link RecommendationService#filterByRecipeCategory(String)}
     */
    @Test
    void testFilterByRecipeCategoryDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.filterByRecipeCategory("quick").size());
    }

    /**
     * Tests: Sorts current recommendations by match score and returns non-empty list.
     * <p>
     * Maps to: {@link RecommendationController#sortByMatchScore()} → {@link RecommendationService#sortByMatchScore()}
     */
    @Test
    void testSortByMatchScoreDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.sortByMatchScore().size());
    }

    /**
     * Test: Sorts current recommendations by cook time and returns non-empty list.
     * <p>
     * Maps to: {@link RecommendationController#sortByCookTime()} → {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testSortByCookTimeDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.sortByCookTime().size());
    }
}
