package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import loader.DemoInventoryLoader;
import loader.JsonFoodCatalogLoader;
import loader.JsonRecipeLoader;
import model.FoodCatalogEntry;
import model.FoodItem;
import model.Recipe;
import service.FoodCatalog;
import service.GroceryService;
import service.IFoodCatalog;
import service.IGroceryService;
import service.IPreferenceService;
import service.IRecommendationService;
import service.InventoryService;
import service.PreferenceService;
import service.RecommendationService;
import service.SessionReset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Legacy console/demo composition root.
 * <p>
 * This class wires the same controller/service stack used by the HTTP backend, then starts the
 * text-mode {@link ConsoleApp}. The official product path is Flutter + Spring Boot HTTP, but this
 * console flow is kept for backward compatibility and demos.
 */
public final class SmartFridgeApp {
    /** Classpath food catalog resource. / classpath 食材目录资源。 */
    private static final String FOOD_CATALOG_RESOURCE = "/food_catalog.json";
    /** Classpath recipes resource. / classpath 菜谱资源。 */
    private static final String RECIPE_RESOURCE = "/recipes.json";

    /**
     * When {@code true}, pre-seeds inventory from {@link #DATA_JSON_RESOURCE}. Keep {@code false} for grading default.
     * <p>
     * 为 {@code true} 时从 {@link #DATA_JSON_RESOURCE} 预灌库存。交作业默认请保持 {@code false}。
     */
    private static final boolean LOAD_DEMO_INVENTORY = false;
    /** Demo seed file under {@code src/main/resources}. / {@code src/main/resources} 下的演示种子文件。 */
    private static final String DATA_JSON_RESOURCE = "/data.json";

    private SmartFridgeApp() {
    }

    /**
     * Wires the full stack and runs the interactive console until user quits.
     * <p>
     * 装配完整调用栈并运行交互式控制台直至用户退出。
     *
     * @param args unused / 未使用
     */
    public static void main(String[] args) {
        List<FoodCatalogEntry> catalogEntries = JsonFoodCatalogLoader.loadFromFileSafe(FOOD_CATALOG_RESOURCE);
        List<Recipe> recipes = JsonRecipeLoader.loadFromFileSafe(RECIPE_RESOURCE);
        IFoodCatalog foodCatalog = new FoodCatalog(catalogEntries);
        InventoryService inventoryService = new InventoryService(foodCatalog);
        if (LOAD_DEMO_INVENTORY) {
            try {
                List<FoodItem> demo = DemoInventoryLoader.loadFoodItemsOptional(DATA_JSON_RESOURCE, foodCatalog);
                if (!demo.isEmpty()) {
                    inventoryService.addAllItems(demo);
                }
            } catch (IOException e) {
                System.err.println("Demo inventory not loaded: " + e.getMessage());
            }
        }
        IPreferenceService preferenceService = new PreferenceService();
        IRecommendationService recommendationService = new RecommendationService(recipes, foodCatalog);
        IGroceryService groceryService = new GroceryService(new ArrayList<>());

        InventoryController inventoryController = new InventoryController(inventoryService);
        PreferenceController preferenceController = new PreferenceController(preferenceService);
        RecommendationController recommendationController = new RecommendationController(recommendationService);
        GroceryController groceryController = new GroceryController(
                groceryService,
                () -> SessionReset.clearAll(
                        preferenceService,
                        inventoryService,
                        recommendationService,
                        groceryService));

        ConsoleApp consoleApp = new ConsoleApp(
                inventoryController,
                preferenceController,
                recommendationController,
                groceryController,
                foodCatalog);
        consoleApp.runInteractive(System.in);
    }
}
