package api.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * Creates the Spring beans used by the Smart Fridge HTTP API.
 *
 * This configuration is similar to {@link ui.SmartFridgeApp},
 * but it builds the objects for the Spring Boot version.
 */
@Configuration
public class FridgeBeansConfig {

    private static final String FOOD_CATALOG_RESOURCE = "/food_catalog.json";
    private static final String RECIPE_RESOURCE = "/recipes.json";
    private static final String DATA_JSON_RESOURCE = "/data.json";

    /**
     * Loads demo inventory data when the API starts.
     */
    private static final boolean LOAD_DEMO_INVENTORY = true;

    @Bean
    public IFoodCatalog foodCatalog() {
        List<FoodCatalogEntry> catalogEntries =
                JsonFoodCatalogLoader.loadFromFileSafe(FOOD_CATALOG_RESOURCE);
        return new FoodCatalog(catalogEntries);
    }

    @Bean
    public InventoryService inventoryService(IFoodCatalog foodCatalog) {
        InventoryService service = new InventoryService(foodCatalog);

        if (LOAD_DEMO_INVENTORY) {
            try {
                List<FoodItem> demoItems =
                        DemoInventoryLoader.loadFoodItemsOptional(DATA_JSON_RESOURCE, foodCatalog);

                if (!demoItems.isEmpty()) {
                    service.addAllItems(demoItems);
                }
            } catch (IOException e) {
                System.err.println("Demo inventory not loaded for API: " + e.getMessage());
            }
        }

        return service;
    }

    @Bean
    public IPreferenceService preferenceService() {
        return new PreferenceService();
    }

    @Bean
    public RecommendationService recommendationService(IFoodCatalog foodCatalog) {
        List<Recipe> recipes = JsonRecipeLoader.loadFromFileSafe(RECIPE_RESOURCE);
        return new RecommendationService(recipes, foodCatalog);
    }

    @Bean
    public IGroceryService groceryService() {
        return new GroceryService(new ArrayList<>());
    }

    @Bean
    public InventoryController inventoryController(InventoryService inventoryService) {
        return new InventoryController(inventoryService);
    }

    @Bean
    public PreferenceController preferenceController(IPreferenceService preferenceService) {
        return new PreferenceController(preferenceService);
    }

    @Bean
    public RecommendationController recommendationController(
            IRecommendationService recommendationService) {
        return new RecommendationController(recommendationService);
    }

    @Bean
    public GroceryController groceryController(
            IGroceryService groceryService,
            IPreferenceService preferenceService,
            InventoryService inventoryService,
            IRecommendationService recommendationService) {
        return new GroceryController(
                groceryService,
                () -> SessionReset.clearAll(
                        preferenceService,
                        inventoryService,
                        recommendationService,
                        groceryService));
    }
}