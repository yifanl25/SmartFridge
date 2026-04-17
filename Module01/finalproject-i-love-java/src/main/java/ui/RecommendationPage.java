package ui;

import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodItem;
import model.Preference;
import model.Recipe;

import java.util.List;

/**
 * Stub Recommendation list: filter by {@link model.RecipeCategory} only; sorting delegated to controller.
 * <p>
 * 推荐列表占位：仅按 {@link model.RecipeCategory} 筛选；排序委托控制器。
 */
@SuppressWarnings("unused")
public class RecommendationPage {
    private final RecommendationController recommendationController;
    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;

    /**
     * @param recommendationController recommendation / 推荐
     * @param inventoryController      inventory for refresh context / 用于刷新推荐时的库存上下文
     * @param preferenceController     preference / 偏好
     */
    public RecommendationPage(
            RecommendationController recommendationController,
            InventoryController inventoryController,
            PreferenceController preferenceController) {
        this.recommendationController = recommendationController;
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
    }

    /**
     * Placeholder render; real UI would show match score, cook time, rating, missing counts.
     * <p>
     * 占位渲染；真实 UI 应显示匹配分、烹饪时间、评分、缺失数等。
     */
    public void render() {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        List<Recipe> recipes = recommendationController.getRecommendations(inventory, preference);
        System.out.println("Recommendation Page (" + recipes.size() + " recipes)");
    }
}
