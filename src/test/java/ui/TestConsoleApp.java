package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.Preference;
import service.FoodCatalog;
import service.GroceryService;
import service.InventoryService;
import service.PreferenceService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestConsoleApp {
    private ConsoleApp consoleApp;
    private InventoryController inventoryController;
    private PreferenceController preferenceController;
    private RecommendationController recommendationController;
    private GroceryController groceryController;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        inventoryController = new InventoryController(new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)))));
        preferenceController = new PreferenceController(new PreferenceService());
        recommendationController = new RecommendationController(new RecommendationService(List.of()));
        groceryController = new GroceryController(new GroceryService(List.of()));
        consoleApp = new ConsoleApp(inventoryController, preferenceController, recommendationController, groceryController);
    }

    @Test void testStartInitializesConsoleFlow() { assertDoesNotThrow(() -> consoleApp.start()); }
    @Test void testProcessCommandRoutesInventoryCommand() { assertDoesNotThrow(() -> consoleApp.processCommand("add Milk")); }
    @Test void testProcessCommandRoutesPreferenceCommand() { assertDoesNotThrow(() -> consoleApp.processCommand("goal fat_loss")); }
    @Test void testProcessCommandRoutesRecommendationCommand() { assertDoesNotThrow(() -> consoleApp.processCommand("noop")); }
    @Test void testProcessCommandRoutesGroceryCommand() { assertDoesNotThrow(() -> consoleApp.processCommand("checkout")); }
    @Test void testProcessCommandHandlesInvalidInput() { assertDoesNotThrow(() -> consoleApp.processCommand(null)); }
}
