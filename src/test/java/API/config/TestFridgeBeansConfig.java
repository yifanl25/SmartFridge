package API.config;

import api.config.FridgeBeansConfig;
import controller.CatalogController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import service.FoodCatalog;
import service.GroceryService;
import service.IFoodCatalog;
import service.IGroceryService;
import service.IPreferenceService;
import service.InventoryService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FridgeBeansConfig.
 * Directly invokes each @Bean method to verify the returned object is non-null.
 */
public class TestFridgeBeansConfig {

    private FridgeBeansConfig config;

    @BeforeEach
    void setUp() {
        config = new FridgeBeansConfig();
    }

    /**
     * Tests that foodCatalog() returns a non-null IFoodCatalog instance.
     */
    @Test
    void testFoodCatalogBeanIsNotNull() {
        IFoodCatalog result = config.foodCatalog();
        assertNotNull(result);
        assertInstanceOf(FoodCatalog.class, result);
    }

    /**
     * Tests that catalogController() returns a non-null CatalogController.
     */
    @Test
    void testCatalogControllerBeanIsNotNull() {
        IFoodCatalog foodCatalog = config.foodCatalog();
        CatalogController result = config.catalogController(foodCatalog);
        assertNotNull(result);
    }

    /**
     * Tests that groceryService() returns a non-null IGroceryService with empty list.
     */
    @Test
    void testGroceryServiceBeanIsNotNull() {
        IGroceryService result = config.groceryService();
        assertNotNull(result);
        assertInstanceOf(GroceryService.class, result);
        assertEquals(0, result.getItems().size());
    }

    /**
     * Tests that inventoryController() returns a non-null InventoryController.
     */
    @Test
    void testInventoryControllerBeanIsNotNull() {
        IFoodCatalog foodCatalog = config.foodCatalog();
        InventoryService inventoryService = config.inventoryService(foodCatalog);
        InventoryController result = config.inventoryController(inventoryService);
        assertNotNull(result);
    }

    /**
     * Tests that preferenceController() returns a non-null PreferenceController.
     */
    @Test
    void testPreferenceControllerBeanIsNotNull() {
        IPreferenceService preferenceService = config.preferenceService();
        PreferenceController result = config.preferenceController(preferenceService);
        assertNotNull(result);
    }

    /**
     * Tests that recommendationController() returns a non-null RecommendationController.
     */
    @Test
    void testRecommendationControllerBeanIsNotNull() {
        IFoodCatalog foodCatalog = config.foodCatalog();
        RecommendationService recommendationService = config.recommendationService(foodCatalog);
        RecommendationController result = config.recommendationController(recommendationService);
        assertNotNull(result);
    }


}