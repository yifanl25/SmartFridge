package ui;

import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodItem;
import model.Preference;
import model.Recipe;
import service.IFoodCatalog;

import java.util.List;

/**
 * Recipe detail demo view.
 * 这个页面不是正式前端页面，而是在控制台里模拟“菜谱详情页”会展示什么。
 */
@SuppressWarnings("unused")
public class RecipeDetailPage {
    private final RecommendationController recommendationController;
    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final IFoodCatalog foodCatalog;

    public RecipeDetailPage(
            RecommendationController recommendationController,
            InventoryController inventoryController,
            PreferenceController preferenceController,
            IFoodCatalog foodCatalog) {
        this.recommendationController = recommendationController;
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.foodCatalog = foodCatalog;
    }

    public void render() {
        renderFirstRecipe();
    }

    /**
     * 直接拿第一道推荐菜，演示 detail 页面会显示什么。
     */
    public void renderFirstRecipe() {
        List<FoodItem> inventory = inventoryController.getVisibleItems();
        Preference preference = preferenceController.getPreference();
        List<Recipe> recipes = recommendationController.getRecommendations(inventory, preference);

        if (recipes.isEmpty()) {
            System.out.println("[Recipe Detail Page]");
            System.out.println("No recipe available.");
            return;
        }

        Recipe recipe = recipes.get(0);

        System.out.println("[Recipe Detail Page]");
        System.out.println(recipe.getTitle() + " | " + recipe.getRecipeCategory().getName());
        System.out.println("Description: " + recipe.getDescription());
        System.out.println("Available required ingredients: " + recipe.getAvailableIngredients());
        System.out.println("Missing required ingredients: " + recipe.getMissingIngredients());

        if (!recipe.getMissingIngredients().isEmpty()) {
            System.out.println("Catalog-normalized missing preview:");
            recipe.getMissingIngredients().forEach(name ->
                    System.out.println("- " + foodCatalog.canonicalFoodName(name)));
        }
    }
}
