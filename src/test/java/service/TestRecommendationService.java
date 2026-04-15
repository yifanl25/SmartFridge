package service;

import model.HealthGoal;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecommendationService {
    private RecommendationService recommendationService;
    private List<Recipe> recipes;

    @BeforeEach
    void setUp() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        recipes = List.of(
                new Recipe("1", "A", cat, 0.9, 4.5, 10, 200, "d", List.of("Egg"), List.of()),
                new Recipe("2", "B", cat, 0.5, 4.1, 20, 300, "d", List.of("Milk"), List.of()));
        recommendationService = new RecommendationService(recipes);
    }

    @Test void testGetRecommendationsUsesInventoryAndPreference() { assertEquals(2, recommendationService.getRecommendations(List.of(), new Preference("p", HealthGoal.FAT_LOSS)).size()); }
    @Test void testFilterByRecipeCategoryReturnsMatchingRecipes() { recommendationService.getRecommendations(List.of(), null); assertEquals(2, recommendationService.filterByRecipeCategory("quick").size()); }
    @Test void testSortByMatchScoreOrdersDescending() { recommendationService.getRecommendations(List.of(), null); assertEquals("A", recommendationService.sortByMatchScore().get(0).getTitle()); }
    @Test void testSortByCookTimeOrdersAscending() { recommendationService.getRecommendations(List.of(), null); assertEquals("A", recommendationService.sortByCookTime().get(0).getTitle()); }
    @Test void testClearRecommendationsRemovesCurrentResults() { recommendationService.getRecommendations(List.of(), null); recommendationService.clearRecommendations(); assertEquals(0, recommendationService.sortByCookTime().size()); }
}
