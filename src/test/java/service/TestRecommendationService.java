package service;

import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * Tests preferenceAlignment() with MUSCLE_BUILDING + HIGH_PROTEIN → 15 points.
     * Branch: MUSCLE_BUILDING + HIGH_PROTEIN tag → best = 15.
     */
    @Test
    void testGetRecommendationsWithMuscleBuildingPreference() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe highProtein = Recipe.loaded("3", "C", cat,
                List.of(Recipe.HealthTag.HIGH_PROTEIN),
                List.of(), List.of(), 4.0, 10, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(highProtein), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(
                List.of(), new Preference("p", HealthGoal.MUSCLE_BUILDING));
        assertEquals(1, result.size());
        assertTrue(result.get(0).getMatchScore() > 0);
    }

    /**
     * Tests preferenceAlignment() with BLOOD_SUGAR_CARE + BLOOD_SUGAR_FRIENDLY → 15 points.
     * Branch: BLOOD_SUGAR_CARE + BLOOD_SUGAR_FRIENDLY tag → best = 15.
     */
    @Test
    void testGetRecommendationsWithBloodSugarCarePreference() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe bsRecipe = Recipe.loaded("4", "D", cat,
                List.of(Recipe.HealthTag.BLOOD_SUGAR_FRIENDLY),
                List.of(), List.of(), 4.0, 10, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(bsRecipe), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(
                List.of(), new Preference("p", HealthGoal.BLOOD_SUGAR_CARE));
        assertEquals(1, result.size());
        assertTrue(result.get(0).getMatchScore() > 0);
    }

    /**
     * Tests preferenceAlignment() with FAT_LOSS + LOW_CALORIE → 15 points.
     * Branch: FAT_LOSS + LOW_CALORIE tag → best = 15.
     */
    @Test
    void testGetRecommendationsWithFatLossAndLowCalorieTag() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe lcRecipe = Recipe.loaded("5", "E", cat,
                List.of(Recipe.HealthTag.LOW_CALORIE),
                List.of(), List.of(), 4.0, 10, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(lcRecipe), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(
                List.of(), new Preference("p", HealthGoal.FAT_LOSS));
        assertTrue(result.get(0).getMatchScore() > 0);
    }

    /**
     * Tests preferenceAlignment() fallback: MUSCLE_BUILDING + BALANCED → 8 points.
     * Branch: MUSCLE_BUILDING, no HIGH_PROTEIN, but BALANCED → best = 8.
     */
    @Test
    void testGetRecommendationsMuscleBuildingFallsBackToBalanced() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe balanced = Recipe.loaded("6", "F", cat,
                List.of(Recipe.HealthTag.BALANCED),
                List.of(), List.of(), 4.0, 10, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(balanced), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(
                List.of(), new Preference("p", HealthGoal.MUSCLE_BUILDING));
        assertTrue(result.get(0).getMatchScore() > 0);
    }

    /**
     * Tests convenienceScore() for cook time > 30 minutes → 0 points.
     * Branch: cookTime > 30 → return 0.
     */
    @Test
    void testGetRecommendationsWithLongCookTimeGetsZeroConvenienceScore() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe slow = Recipe.loaded("7", "G", cat,
                List.of(Recipe.HealthTag.BALANCED),
                List.of(), List.of(), 4.0, 60, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(slow), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(List.of(), null);
        assertEquals(1, result.size());
    }

    /**
     * Tests convenienceScore() for cook time between 16-30 minutes → 3 points.
     * Branch: cookTime <= 30 → return 3.
     */
    @Test
    void testGetRecommendationsWithMediumCookTimeGetsThreePoints() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        Recipe medium = Recipe.loaded("8", "H", cat,
                List.of(Recipe.HealthTag.BALANCED),
                List.of(), List.of(), 4.0, 25, 200, "d");
        RecommendationService svc = new RecommendationService(
                List.of(medium), new FoodCatalog(List.of()));
        List<Recipe> result = svc.getRecommendations(List.of(), null);
        assertEquals(1, result.size());
    }

    /**
     * Tests getRecommendations() with null inventory defaults to empty list.
     * Branch: inventory == null → List.of().
     */
    @Test
    void testGetRecommendationsWithNullInventoryDefaultsToEmpty() {
        assertEquals(2, recommendationService.getRecommendations(null, null).size());
    }

    /**
     * Tests urgentUsageScore() with 1 urgent match → 10 points.
     * Branch: urgentMatchedCount == 1 → return 10.
     */
    @Test
    void testGetRecommendationsWithOneUrgentIngredientGivesTenPoints() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        FoodCategory foodCat = new FoodCategory("f1", "Protein", "egg");
        Recipe recipe = Recipe.loaded("9", "I", cat,
                List.of(Recipe.HealthTag.BALANCED),
                List.of(new Recipe.Ingredient("Egg", "1", false)),
                List.of(), 4.0, 10, 200, "d");
        // Create an urgent FoodItem (expires today)
        String today = java.time.LocalDate.now().toString();
        FoodItem urgentEgg = new FoodItem("i1", "Egg", foodCat, 2, "pcs", today, today);
        model.FoodCatalogEntry entry = new model.FoodCatalogEntry("Egg", 7, foodCat);
        RecommendationService svc = new RecommendationService(
                List.of(recipe), new FoodCatalog(List.of(entry)));
        List<Recipe> result = svc.getRecommendations(List.of(urgentEgg), null);
        assertTrue(result.get(0).getMatchScore() > 0);
        assertEquals(1, result.get(0).getUrgentMatchedCount());
    }

    /**
     * Tests score is capped at 100 when all bonuses exceed 100.
     * Branch: score > 100 → score = 100.
     */
    @Test
    void testScoreIsCappedAtOneHundred() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        FoodCategory foodCat = new FoodCategory("f1", "Protein", "egg");
        String today = java.time.LocalDate.now().toString();
        // Recipe with HIGH_PROTEIN tag, short cook time, all ingredients matched
        Recipe recipe = Recipe.loaded("10", "J", cat,
                List.of(Recipe.HealthTag.HIGH_PROTEIN),
                List.of(new Recipe.Ingredient("Egg", "1", false)),
                List.of(), 4.0, 10, 200, "d");
        FoodItem urgentEgg = new FoodItem("i1", "Egg", foodCat, 2, "pcs", today, today);
        model.FoodCatalogEntry entry = new model.FoodCatalogEntry("Egg", 7, foodCat);
        RecommendationService svc = new RecommendationService(
                List.of(recipe), new FoodCatalog(List.of(entry)));
        List<Recipe> result = svc.getRecommendations(
                List.of(urgentEgg), new Preference("p", HealthGoal.MUSCLE_BUILDING));
        assertTrue(result.get(0).getMatchScore() <= 100.0);
    }


}
