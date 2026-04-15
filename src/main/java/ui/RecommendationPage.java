package ui;

import controller.RecommendationController;

public class RecommendationPage {
    // Controller gateway for recommendation query/filter/sort actions.
    private final RecommendationController recommendationController;

    // Wire recommendation page to controller.
    public RecommendationPage(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }

    // Render recommendation card list.
    // PRD notes:
    // - show title, recipeCategory, matchScore, cookTime, rating, available/missing counts
    // - filter by RecipeCategory only
    // - recipe card click routes to RecipeDetail
    public void render() {
        System.out.println("Recommendation Page");
    }
}
