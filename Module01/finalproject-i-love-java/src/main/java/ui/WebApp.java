package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import service.IFoodCatalog;

/**
 * Optional web/Figma shell entry: holds controller references and starts a scripted text walk-through.
 * Current course demo can still use ConsoleApp from SmartFridgeApp instead.
 */
@SuppressWarnings("unused")
public class WebApp {
    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;
    private final IFoodCatalog foodCatalog;

    public WebApp(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController,
            IFoodCatalog foodCatalog) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
        this.foodCatalog = foodCatalog;
    }

    /**
     * 这个方法不是做真网页，而是在控制台里把网页流程演示一遍。
     */
    public void start() {
        System.out.println();
        System.out.println("=== Smart Fridge pseudo-web demo ===");

        new WelcomePage().render();
        new PreferencePage(preferenceController).render();
        new InventoryPage(inventoryController).render();
        new RecommendationPage(
                recommendationController,
                inventoryController,
                preferenceController
        ).render();

        new RecipeDetailPage(
                recommendationController,
                inventoryController,
                preferenceController,
                foodCatalog
        ).render();

        new GroceryPage(groceryController).render();

        System.out.println("=== End of pseudo-web demo ===");
        System.out.println();
    }
}
