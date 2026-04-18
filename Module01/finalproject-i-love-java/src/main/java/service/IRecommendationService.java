package service;

import model.FoodItem;
import model.Preference;
import model.Recipe;

import java.util.List;

/**
 * Computes and stores scored recipe recommendations for the current session.
 * <p>
 * 计算并缓存本会话内的带分推荐菜谱列表。
 */
public interface IRecommendationService {

    /**
     * Scores each template recipe from inventory + preference; refreshes cached list and returns a copy.
     * Main coverage uses required ingredients only (per PRD).
     * <p>
     * 根据库存与偏好为每条模板菜谱打分；刷新缓存并返回副本。主覆盖度仅使用必选食材（PRD）。
     */
    List<Recipe> getRecommendations(List<FoodItem> inventory, Preference preference);

    /**
     * Filters cached recommendations by {@link model.RecipeCategory} name substring (case-insensitive).
     * Do not use {@link model.FoodCategory} here.
     * <p>
     * 按 {@link model.RecipeCategory} 名称子串（忽略大小写）筛选当前缓存。勿使用 {@link model.FoodCategory}。
     */
    List<Recipe> filterByRecipeCategory(String categoryName);

    /**
     * Returns cached recommendations sorted by match score descending with PRD tie-breaks.
     * <p>
     * 返回按匹配分降序且带 PRD 决胜规则的当前缓存排序结果。
     */
    List<Recipe> sortByMatchScore();

    /**
     * Returns cached recommendations sorted primarily by cook time ascending.
     * <p>
     * 返回以烹饪时间升序为主排序的当前缓存结果。
     */
    List<Recipe> sortByCookTime();

    /**
     * Clears cached recommendations at checkout / session reset.
     * <p>
     * 在结账/会话重置时清空推荐缓存。
     */
    void clearRecommendations();
}
