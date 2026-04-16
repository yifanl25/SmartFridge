package ui;

import controller.RecommendationController;

/**
 * Stub Recipe detail view (navigation to grocery from missing ingredients in full UI).
 * <p>
 * 菜谱详情占位（完整 UI 中可由缺失食材跳转购物）。
 */
@SuppressWarnings("unused")
public class RecipeDetailPage {
    private final RecommendationController recommendationController;

    /**
     * @param recommendationController recommendation controller / 推荐控制器
     */
    public RecipeDetailPage(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }

    /** Placeholder render. / 占位渲染。 */
    public void render() {
        // 这里应该显示一道菜的详情；缺的东西可以一键加购物车 — 用 recommendationController，别新写打分代码。
        // Show one recipe; "add missing to cart" uses controller. 食材类≠菜谱类 / Food vs recipe categories stay separate.
        // INSERT YOUR CODE HERE
        System.out.println("Recipe Detail Page");
    }
}
