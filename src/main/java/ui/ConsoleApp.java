package ui;

import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.HealthGoal;

public class ConsoleApp {
    // Optional shell controller for inventory commands (architecture-only).
    private final InventoryController inventoryController;
    // Optional shell controller for preference commands.
    private final PreferenceController preferenceController;
    // Optional shell controller for recommendation commands.
    private final RecommendationController recommendationController;
    // Optional shell controller for grocery commands.
    private final GroceryController groceryController;

    // Console shell wiring constructor.
    public ConsoleApp(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
    }

    // Starts optional CLI shell (not part of main visual demo flow).
    public void start() {
        System.out.println("Console started.");
    }

    // Lightweight command router used for teacher-style demo interactions.
    // PRD note: ConsoleApp is optional and not required for Figma visual flow.
    public void processCommand(String input) {
        if (input == null) {
            return;
        }
        String command = input.trim().toLowerCase();
        if (command.startsWith("add ")) {
            inventoryController.addItem(input.substring(4));
        } else if (command.startsWith("goal ")) {
            preferenceController.savePreference(HealthGoal.valueOf(input.substring(5).trim().toUpperCase()));
        } else if (command.equals("checkout")) {
            groceryController.checkout();
        }
    }
}
