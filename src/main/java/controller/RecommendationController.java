package controller;

import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IRecommendationService;

import java.util.List;

/**
 * MVC controller for Recommendation and Recipe Detail flows; delegates scoring to {@link IRecommendationService}.
 * <p>
 * 推荐与菜谱详情流程控制器；打分逻辑委托 {@link IRecommendationService}。
 * <p>老图里可能写 filterByCategory，代码里菜谱类叫 {@link #filterByRecipeCategory(String)} — 名字不一样，意思一样。</p>
 * <p>手机要更多 URL：看 {@link api.web.RecommendationApiController}。</p>
 */
public class RecommendationController {
    /** Recommendation engine and cache. / 推荐引擎与缓存。 */
    private final IRecommendationService recommendationService;

    /**
     * @param recommendationService injected implementation / 注入的实现
     */
    public RecommendationController(IRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Refreshes scored recommendations from inventory + preference.
     * <p>
     * 根据库存与偏好刷新带分推荐。
     */
    public List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference) {
        return recommendationService.getRecommendations(inventory, preference);
    }

    /**
     * Filters current cache by {@link model.RecipeCategory} substring (never {@link model.FoodCategory}).
     * <p>
     * 按 {@link model.RecipeCategory} 子串筛选当前缓存（勿用 {@link model.FoodCategory}）。
     */
    public List<Recipe> filterByRecipeCategory(String categoryName) {
        return recommendationService.filterByRecipeCategory(categoryName);
    }

    /**
     * Sorts current cache by match score with PRD tie-breaks.
     * <p>
     * 按匹配分及 PRD 决胜规则排序当前缓存。
     */
    public List<Recipe> sortByMatchScore() {
        return recommendationService.sortByMatchScore();
    }

    /**
     * Sorts current cache primarily by cook time ascending.
     * <p>
     * 以烹饪时间升序为主排序当前缓存。
     */
    public List<Recipe> sortByCookTime() {
        return recommendationService.sortByCookTime();
    }
}
