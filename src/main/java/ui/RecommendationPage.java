package ui;

import controller.RecommendationController;

/**
 * Stub Recommendation list: filter by {@link model.RecipeCategory} only; sorting delegated to controller.
 * <p>
 * 推荐列表占位：仅按 {@link model.RecipeCategory} 筛选；排序委托控制器。
 */
@SuppressWarnings("unused")
public class RecommendationPage {
    private final RecommendationController recommendationController;

    /**
     * @param recommendationController recommendation controller / 推荐控制器
     */
    public RecommendationPage(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }

    /**
     * Placeholder render; real UI would show match score, cook time, rating, missing counts.
     * <p>
     * 占位渲染；真实 UI 应显示匹配分、烹饪时间、评分、缺失数等。
     */
    public void render() {
        // 这里应该显示推荐列表，按钮去调 filterByRecipeCategory、sortByMatchScore、sortByCookTime。
        // 算分在 RecommendationService，别在这一页自己算 / Scoring stays in RecommendationService.
        // INSERT YOUR CODE HERE
        System.out.println("Recommendation Page");
    }
}
