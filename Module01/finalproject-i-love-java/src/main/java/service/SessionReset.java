package service;

/**
 * Clears all in-memory session data for the demo.
 */
public final class SessionReset {

    private SessionReset() {
    }

    /**
     * Clears preference, inventory, recommendations, and grocery data.
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
