package service;

/**
 * Coordinates full session teardown after checkout: preference, inventory, recommendations, grocery.
 * <p>
 * 结账后协调完整会话拆除：偏好、库存、推荐、购物清单一并清空（无持久化）。
 */
public final class SessionReset {
    private SessionReset() {
    }

    /**
     * Clears all session-scoped modules in a fixed order suitable for demo loop restart.
     * <p>
     * 按固定顺序清空各会话模块，便于演示循环重新开始。
     */
    public static void clearAll(
            IPreferenceService preferenceService,
            IInventoryService inventoryService,
            IRecommendationService recommendationService,
            IGroceryService groceryService) {
        preferenceService.clearPreference();
        inventoryService.clearInventory();
        recommendationService.clearRecommendations();
        groceryService.clearGrocery();
    }
}
