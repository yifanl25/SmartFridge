package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;

/**
 * Optional web/Figma shell entry: holds controller references and starts {@link WelcomePage} stub.
 * Current course demo uses {@link ConsoleApp} from {@link SmartFridgeApp} instead.
 * <p>
 * 可选的 Web/Figma 壳入口：持有各控制器引用并启动 {@link WelcomePage} 占位。
 * 当前课程演示由 {@link SmartFridgeApp} 启动 {@link ConsoleApp}。
 */
@SuppressWarnings("unused")
public class WebApp {
    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;

    /**
     * @param inventoryController      inventory / 库存
     * @param preferenceController     preference / 偏好
     * @param recommendationController recommendations / 推荐
     * @param groceryController        grocery / 购物
     */
    public WebApp(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
    }

    /**
     * Placeholder: prints welcome; real UI would route to preference/inventory flows.
     * <p>
     * 占位：打印欢迎语；真实 UI 应路由到偏好/库存等流程。
     */
    public void start() {
        // 理想流程 / Happy path: 欢迎 → 选目标 → 看冰箱 → 看推荐 → 菜谱详情 → 购物 — 都用已经注入的四个 controller。
        // Right now 现在: 只打印欢迎语 / only prints welcome. 后端别存「皮肤颜色」这种假数据 / No fake decorative state in backend.
        // INSERT YOUR CODE HERE
        new WelcomePage().render();
    }
}
