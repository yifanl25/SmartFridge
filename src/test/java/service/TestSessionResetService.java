package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.GroceryItem;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD: SessionReset clears all modules.
 */
public class TestSessionResetService {

    private IPreferenceService preferenceService;
    private IInventoryService inventoryService;
    private IRecommendationService recommendationService;
    private IGroceryService groceryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalogEntry entry = new FoodCatalogEntry("Milk", 7, cat);
        FoodCatalog catalog = new FoodCatalog(List.of(entry));

        preferenceService = new PreferenceService();
        inventoryService = new InventoryService(catalog);
        recommendationService = new RecommendationService(List.of(), catalog);
        groceryService = new GroceryService(List.of(
                new GroceryItem("g1", "Milk", cat, 2, 3.0, false)));

    }

    /**
     * Tests clearAll().
     */
    @Test
    void testClearAllResetsAllModules() {
        SessionReset.clearAll(preferenceService, inventoryService, recommendationService, groceryService);

        assertNull(preferenceService.getPreference());
        assertEquals(0, inventoryService.getAllItems().size());
        assertEquals(0, recommendationService.sortByCookTime().size());
        assertEquals(0, groceryService.getItems().size());
    }
    /**
     * Tests that SessionReset cannot be instantiated (private constructor).
     * Covers the private constructor branch via reflection.
     */
    @Test
    void testPrivateConstructorIsInaccessible() throws Exception {
        Constructor<SessionReset> constructor = SessionReset.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertDoesNotThrow(() -> constructor.newInstance());
    }
}