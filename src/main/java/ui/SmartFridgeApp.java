package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import loader.JsonFoodCatalogLoader;
import loader.JsonRecipeLoader;
import model.FoodCatalogEntry;
import model.Recipe;
import service.FoodCatalog;
import service.GroceryService;
import service.IFoodCatalog;
import service.IGroceryService;
import service.IInventoryService;
import service.IPreferenceService;
import service.IRecommendationService;
import service.InventoryService;
import service.PreferenceService;
import service.RecommendationService;

import java.util.ArrayList;
import java.util.List;

public final class SmartFridgeApp {
    // Static catalog source path (PRD: static JSON only, no dynamic persistence).
    private static final String FOOD_CATALOG_FILE = "src/main/resources/food-catalog.json";
    // Static recipe source path (PRD: static JSON only).
    private static final String RECIPE_FILE = "src/main/resources/recipes.json";

    // Utility entry class; no instances required.
    private SmartFridgeApp() {
    }

    // Composition root:
    // wire loaders -> services -> controllers -> app shell.
    // PRD scope note: this bootstraps a single-loop demo runtime.
    public static void main(String[] args) {
        List<FoodCatalogEntry> catalogEntries = JsonFoodCatalogLoader.loadFromFile(FOOD_CATALOG_FILE);
        List<Recipe> recipes = JsonRecipeLoader.loadFromFile(RECIPE_FILE);
        IFoodCatalog foodCatalog = new FoodCatalog(catalogEntries);
        IInventoryService inventoryService = new InventoryService(foodCatalog);
        IPreferenceService preferenceService = new PreferenceService();
        IRecommendationService recommendationService = new RecommendationService(recipes);
        IGroceryService groceryService = new GroceryService(new ArrayList<>());

        InventoryController inventoryController = new InventoryController(inventoryService);
        PreferenceController preferenceController = new PreferenceController(preferenceService);
        RecommendationController recommendationController = new RecommendationController(recommendationService);
        GroceryController groceryController = new GroceryController(groceryService);

        new WebApp(
                inventoryController,
                preferenceController,
                recommendationController,
                groceryController).start();
    }
}
