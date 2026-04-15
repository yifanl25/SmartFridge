package controller;

import model.Recipe;
import model.RecipeCategory;
import service.IRecommendationService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecommendationController {
    private RecommendationController recommendationController;
    private IRecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        recommendationService = new RecommendationService(List.of(
                new Recipe("1", "A", cat, 0.9, 4.5, 10, 200, "d", List.of("Egg"), List.of())));
        recommendationController = new RecommendationController(recommendationService);
    }

    @Test void testGetRecommendationsDelegatesToRecommendationService() { assertEquals(1, recommendationController.getRecommendations(List.of(), null).size()); }
    @Test void testFilterByRecipeCategoryDelegatesToRecommendationService() { recommendationController.getRecommendations(List.of(), null); assertEquals(1, recommendationController.filterByRecipeCategory("quick").size()); }
    @Test void testSortByMatchScoreDelegatesToRecommendationService() { recommendationController.getRecommendations(List.of(), null); assertEquals(1, recommendationController.sortByMatchScore().size()); }
    @Test void testSortByCookTimeDelegatesToRecommendationService() { recommendationController.getRecommendations(List.of(), null); assertEquals(1, recommendationController.sortByCookTime().size()); }
}
