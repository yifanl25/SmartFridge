package service;

/**
 * Coordinates full session teardown after checkout: preference, inventory, recommendations, grocery.
 * <p>
 * the grocery list are all cleared, with no persistence.
 */
public final class SessionReset {
    private SessionReset() {
    }

    /**
     * Clears all session-scoped modules in a fixed order suitable for demo loop restart.
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
