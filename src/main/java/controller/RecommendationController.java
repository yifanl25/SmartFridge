package controller;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IRecommendationService;

import java.util.List;

/**
 * Internal coordination layer for recommendation use cases.
 * <p>
 * The HTTP layer calls into this facade, and the actual scoring/cache logic stays in
 * {@link IRecommendationService}. This preserves the older controller/service split without
 * making {@code controller} itself responsible for HTTP routing.
 */
public class RecommendationController {
    // 真正负责推荐逻辑的是 recommendationService。
    private final IRecommendationService recommendationService;

    public RecommendationController(IRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * 根据当前库存 + 当前偏好，刷新并返回推荐列表。
     */
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        return recommendationService.getRecommendations(inventory, preference);
    }

    /**
     * 按菜谱分类筛选当前推荐缓存。
     *
     * 注意这里筛的是 RecipeCategory，
     * 不是库存那边的 FoodCategory。
     */
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return recommendationService.filterByRecipeCategory(categoryName);
    }

    /**
     * 从当前推荐结果里，根据 recipeId 找出某一条菜谱。
     *
     * 这是 recipe detail endpoint 会用到的帮助方法。
     */
    public Recipe getRecommendationById(List<FoodItem> inventory, Preference preference, String recipeId) {
        return getRecommendations(inventory, preference).stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 按 match score 排序。
     */
    public List<Recipe> sortByMatchScore() {
        return recommendationService.sortByMatchScore();
    }

    /**
     * 按 cook time 排序。
     */
    public List<Recipe> sortByCookTime() {
        return recommendationService.sortByCookTime();
    }
}
