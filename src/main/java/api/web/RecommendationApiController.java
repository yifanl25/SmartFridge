package api.web;

import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodItem;
import model.Preference;
import model.Recipe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 手机看「今天推荐什么菜」/ HTTP recipe recommendations for Flutter.
 * <p>Now 现在: GET 一次列表（用当前冰箱 + 健康目标算分）.</p>
 * <p>Missing 还缺: 按菜谱类筛选、按匹配分排序、按做饭时间排序 — RecommendationController 里<strong>有</strong>方法，还没做成 HTTP。</p>
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationApiController {

    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;

    public RecommendationApiController(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
    }

    // 给做 HTTP 的同学 / For whoever adds more URLs:
    // 要做：筛选 + 排序 — 调 recommendationController 里现成的 filterByRecipeCategory、sortByMatchScore、sortByCookTime。
    // To do: filter + sort — call those existing methods on recommendationController.
    // 「怎么算分」继续在 RecommendationService 里改；<strong>不要</strong>在 api 里再写一遍公式 /
    // Keep scoring math in RecommendationService; <strong>do not</strong> copy formulas into this api class.
    // 一次会话、不存数据库 / One session, no DB. 食材类≠菜谱类 / FoodCategory ≠ RecipeCategory.
    // INSERT YOUR CODE HERE

    /**
     * Refreshes scores from the live session and returns the ranked list.
     */
    @GetMapping
    public List<Recipe> list() {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        return recommendationController.getRecommendations(inventory, preference);
    }

    // 如果 PRD 要「点开一道菜看详情」：加一个 GET，用 id 拿一条 Recipe（只读）.
    // If PRD needs recipe detail: add a read-only GET by id.
    // 不要用后端假装做花里胡哨的 UI 功能 / Don't build fake "pretty UI" as backend logic. 不要数据库 / No DB.
    // INSERT YOUR CODE HERE
}
