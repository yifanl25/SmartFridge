package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.GroceryService;
import service.InventoryService;
import service.PreferenceService;
import service.RecommendationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TDD：Console-based CLI entry point.(Maps to: {@link ConsoleApp}，optional demo flow).
 */
public class TestConsoleApp {
    private ConsoleApp consoleApp;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        InventoryController inventoryController =
                new InventoryController(new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)))));
        PreferenceController preferenceController = new PreferenceController(new PreferenceService());
        RecommendationController recommendationController =
                new RecommendationController(new RecommendationService(List.of(), new FoodCatalog(List.of())));
        GroceryController groceryController = new GroceryController(new GroceryService(List.of()));
        consoleApp = new ConsoleApp(
                inventoryController,
                preferenceController,
                recommendationController,
                groceryController,
                new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat))));
    }

    /**
     * Test：{@link ConsoleApp#start()} initializes without blocking(only prints instructions).
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#start()}
     */
    @Test
    void testStartInitializesConsoleFlow() {
        assertDoesNotThrow(() -> consoleApp.start());
    }

    /**
     * Test：Parses {@code add <name>} and delegates to inventory logic.
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.InventoryController#addItem(String)}
     */
    @Test
    void testProcessCommandRoutesInventoryCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("add Milk"));
    }

    /**
     * Test: Parses {@code goal <goal>} and stores user health preference.
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.PreferenceController#savePreference(model.HealthGoal)}
     */
    @Test
    void testProcessCommandRoutesPreferenceCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("goal fat_loss"));
    }

    /**
     * Test：handle unknown commands(Like: {@code noop}).
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#processCommand(String)}
     */
    @Test
    void testProcessCommandRoutesRecommendationCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("noop"));
    }

    /**
     * Test：Parese {@code checkout}and trigger grocery checkout and session resets.
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#processCommand(String)} → {@link controller.GroceryController#checkout()}
     */
    @Test
    void testProcessCommandRoutesGroceryCommand() {
        assertDoesNotThrow(() -> consoleApp.processCommand("checkout"));
    }

    /**
     * Test：handle null input safely. {@code null}
     * Verification: No exception is thrown.
     * <p>
     * Maps to: {@link ConsoleApp#processCommand(String)}
     */
    @Test
    void testProcessCommandHandlesInvalidInput() {
        assertDoesNotThrow(() -> consoleApp.processCommand(null));
    }
}
