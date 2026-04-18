package controller;

import model.Recipe;
import model.RecipeCategory;
import service.FoodCatalog;
import service.IRecommendationService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：推荐页/控制器边界（对应 {@link RecommendationController} → {@link IRecommendationService}）。
 */
public class TestRecommendationController {
    private RecommendationController recommendationController;
    private IRecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        RecipeCategory cat = new RecipeCategory("r1", "Quick", "bolt");
        recommendationService = new RecommendationService(
                List.of(Recipe.loaded(
                        "1",
                        "A",
                        cat,
                        List.of(Recipe.HealthTag.BALANCED),
                        List.of(new Recipe.Ingredient("Egg", "1", false)),
                        List.of(),
                        4.5,
                        10,
                        200,
                        "d")),
                new FoodCatalog(List.of()));
        recommendationController = new RecommendationController(recommendationService);
    }

    /**
     * 测试功能：根据库存与偏好计算推荐列表。
     * 验证点：委托服务并返回预期条数。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationController#getRecommendations(java.util.List, model.Preference)} → {@link RecommendationService#getRecommendations(java.util.List, model.Preference)}
     */
    @Test
    void testGetRecommendationsDelegatesToRecommendationService() {
        assertEquals(1, recommendationController.getRecommendations(List.of(), null).size());
    }

    /**
     * 测试功能：按菜谱分类（{@link RecipeCategory}）筛选当前推荐结果。
     * 验证点：先刷新推荐再筛选，条数符合。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationController#filterByRecipeCategory(String)} → {@link RecommendationService#filterByRecipeCategory(String)}
     */
    @Test
    void testFilterByRecipeCategoryDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.filterByRecipeCategory("quick").size());
    }

    /**
     * 测试功能：按匹配分排序当前推荐列表。
     * 验证点：委托服务且返回非空列表。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationController#sortByMatchScore()} → {@link RecommendationService#sortByMatchScore()}
     */
    @Test
    void testSortByMatchScoreDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.sortByMatchScore().size());
    }

    /**
     * 测试功能：按烹饪时间排序当前推荐列表。
     * 验证点：委托服务且返回非空列表。
     * <p>
     * 对应源码 / Maps to: {@link RecommendationController#sortByCookTime()} → {@link RecommendationService#sortByCookTime()}
     */
    @Test
    void testSortByCookTimeDelegatesToRecommendationService() {
        recommendationController.getRecommendations(List.of(), null);
        assertEquals(1, recommendationController.sortByCookTime().size());
    }
}
