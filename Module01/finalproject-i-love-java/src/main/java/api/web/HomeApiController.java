package api.web;

import api.dto.HomeDashboardResponse;
import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodItem;
import model.GroceryItem;
import model.Preference;
import model.Recipe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns simple aggregated data for the demo home dashboard.
 */
@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;

    public HomeApiController(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
    }

    @GetMapping
    public HomeDashboardResponse dashboard() {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        List<Recipe> recommendations =
                recommendationController.getRecommendations(inventory, preference);
        List<GroceryItem> grocery = groceryController.getItems();

        double subtotal = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(subtotal);
        double total = groceryController.calculateTotal(subtotal, tax);

        int urgentInventoryCount = 0;
        int collectedGroceryCount = 0;
        List<String> expiringSoon = new ArrayList<>();
        List<String> topRecipeTitles = new ArrayList<>();

        for (FoodItem item : inventory) {
            if (item.isUrgent()) {
                urgentInventoryCount++;

                if (expiringSoon.size() < 5) {
                    expiringSoon.add(item.getName());
                }
            }
        }

        for (GroceryItem item : grocery) {
            if (item.isCollected()) {
                collectedGroceryCount++;
            }
        }

        for (Recipe recipe : recommendations) {
            if (topRecipeTitles.size() < 3) {
                topRecipeTitles.add(recipe.getTitle());
            } else {
                break;
            }
        }

        HomeDashboardResponse response = new HomeDashboardResponse();
        response.setInventoryCount(inventory.size());
        response.setUrgentInventoryCount(urgentInventoryCount);
        response.setGroceryCount(grocery.size());
        response.setCollectedGroceryCount(collectedGroceryCount);
        response.setCurrentHealthGoal(
                preference == null ? null : preference.getHealthGoal().name());
        response.setSubtotal(subtotal);
        response.setTax(tax);
        response.setTotal(total);
        response.setExpiringSoon(expiringSoon);
        response.setTopRecipeTitles(topRecipeTitles);

        return response;
    }
}
