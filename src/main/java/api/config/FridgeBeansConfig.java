package api.config;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import loader.JsonFoodCatalogLoader;
import loader.JsonRecipeLoader;
import model.FoodCatalogEntry;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 把「冰箱程序」需要的 Java 对象一个个造出来，给 Spring 用 / Builds the same objects as the console app, for HTTP.
 * <p>和 {@link ui.SmartFridgeApp} 很像，但是给网页接口跑的那一份。</p>
 */
@Configuration
public class FridgeBeansConfig {

    private static final String FOOD_CATALOG_RESOURCE = "/food_catalog.json";
    private static final String RECIPE_RESOURCE = "/recipes.json";

    @Bean
    public IFoodCatalog foodCatalog() {
        List<FoodCatalogEntry> catalogEntries = JsonFoodCatalogLoader.loadFromFileSafe(FOOD_CATALOG_RESOURCE);
        return new FoodCatalog(catalogEntries);
    }

    @Bean
    public InventoryService inventoryService(IFoodCatalog foodCatalog) {
        // 小差别 / Small gap: 控制台 SmartFridgeApp 里有个开关 LOAD_DEMO_INVENTORY，开了会从 data.json 塞演示食材。
        // Console can preload demo food from data.json; HTTP 这边<strong>还没</strong>做同样的事。
        // 想让两边一开始一样，就在这里抄那段逻辑；不想就写 README 说明 / Copy seed logic here, or document the difference.
        // INSERT YOUR CODE HERE
        return new InventoryService(foodCatalog);
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
    public RecommendationController recommendationController(IRecommendationService recommendationService) {
        return new RecommendationController(recommendationService);
    }

    @Bean
    public GroceryController groceryController(
            IGroceryService groceryService,
            IPreferenceService preferenceService,
            InventoryService inventoryService,
            IRecommendationService recommendationService) {
        // 结账后会跑 SessionReset = 整局清空（一次 demo 一圈）/ After checkout, SessionReset clears everything (one loop).
        // 想改「只清购物车」要先全队同意再动这里 / Change wiring only if whole team agrees.
        // INSERT YOUR CODE HERE
        return new GroceryController(
                groceryService,
                () -> SessionReset.clearAll(
                        preferenceService,
                        inventoryService,
                        recommendationService,
                        groceryService));
    }
}
