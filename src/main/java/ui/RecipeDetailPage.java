package ui;

import controller.RecommendationController;

public class RecipeDetailPage {
    // Controller gateway to read selected recipe detail context.
    private final RecommendationController recommendationController;

    // Wire detail page to recommendation controller.
    public RecipeDetailPage(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }

    // Render recipe detail panel.
    // PRD interaction notes:
    // - Start Cooking is decorative only
    // - breadcrumb/back controls are decorative
    // - path to Grocery comes from missing ingredients route
    public void render() {
        System.out.println("Recipe Detail Page");
    }
}
