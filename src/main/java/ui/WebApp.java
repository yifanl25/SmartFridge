package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;

public class WebApp {
    // Inventory route actions are delegated through this controller.
    private final InventoryController inventoryController;
    // Preference setup interactions are delegated through this controller.
    private final PreferenceController preferenceController;
    // Recommendation page interactions are delegated through this controller.
    private final RecommendationController recommendationController;
    // Grocery page interactions are delegated through this controller.
    private final GroceryController groceryController;

    // Web shell wiring constructor.
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

    // Starts visual flow at Welcome page.
    // PRD route entry: Welcome -> Get Started Free -> Preference/Inventory flow.
    public void start() {
        new WelcomePage().render();
    }
}
