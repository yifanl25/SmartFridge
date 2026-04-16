package service;

import model.HealthGoal;
import model.Preference;
import model.Recipe;
import model.RecipeCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：推荐算法、排序与筛选（对应 {@link RecommendationService}，PRD 打分与 tie-break）。
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
     * 测试功能：综合库存与偏好生成带分推荐列表。
     * 验证点：返回全部模板菜谱条数（本 fixture 为 2）。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationService#getRecommendations(java.util.List, model.Preference)}
     */
    @Test
    void testGetRecommendationsUsesInventoryAndPreference() {
        assertEquals(2, recommendationService.getRecommendations(List.of(), new Preference("p", HealthGoal.FAT_LOSS)).size());
    }

    /**
     * 测试功能：按菜谱分类名称（子串）筛选当前推荐缓存。
     * 验证点：{@code quick} 匹配两条 fixture。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationService#filterByRecipeCategory(String)}
     */
    @Test
    void testFilterByRecipeCategoryReturnsMatchingRecipes() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals(2, recommendationService.filterByRecipeCategory("quick").size());
    }

    /**
     * 测试功能：按匹配分降序排列当前推荐。
     * 验证点：分高者 {@code A} 在首位。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationService#sortByMatchScore()}
     */
    @Test
    void testSortByMatchScoreOrdersDescending() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals("A", recommendationService.sortByMatchScore().get(0).getTitle());
    }

    /**
     * 测试功能：按烹饪时间升序排列当前推荐（时间短优先）。
     * 验证点：10 分钟菜谱 {@code A} 在 20 分钟 {@code B} 之前。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testSortByCookTimeOrdersAscending() {
        recommendationService.getRecommendations(List.of(), null);
        assertEquals("A", recommendationService.sortByCookTime().get(0).getTitle());
    }

    /**
     * 测试功能：清空当前推荐缓存（结账/会话重置链路）。
     * 验证点：清空后再排序结果为空。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationService#clearRecommendations()}, {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testClearRecommendationsRemovesCurrentResults() {
        recommendationService.getRecommendations(List.of(), null);
        recommendationService.clearRecommendations();
        assertEquals(0, recommendationService.sortByCookTime().size());
    }
}
