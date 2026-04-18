package api.dto;

import model.FoodItem;
import model.Recipe;
import service.IFoodCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Response DTO for the recipe detail page.
 */
public class RecipeDetailResponse {

    private String id;
    private String title;
    private String category;
    private double matchScore;
    private int matchPercent;
    private double rating;
    private int cookTime;
    private int calories;
    private String description;
    private int urgentMatchedCount;
    private List<String> availableIngredients;
    private List<String> missingIngredients;
    private List<RecipeIngredientStatusResponse> requiredIngredients;
    private List<RecipeIngredientStatusResponse> optionalIngredients;

    /**
     * Builds a detail response from one recipe and the current inventory.
     */
    public static RecipeDetailResponse from(
            Recipe recipe,
            List<FoodItem> inventory,
            IFoodCatalog foodCatalog) {

        RecipeDetailResponse response = new RecipeDetailResponse();
        response.id = recipe.getId();
        response.title = recipe.getTitle();
        response.category = recipe.getRecipeCategory().getName();
        response.matchScore = recipe.getMatchScore();

        int totalRequired = recipe.getRequiredIngredients().size();
        int matchedRequired = recipe.getAvailableIngredients().size();

        if (totalRequired == 0) {
            response.matchPercent = 0;
        } else {
            response.matchPercent =
                    (int) Math.round((double) matchedRequired * 100.0 / totalRequired);
        }

        response.rating = recipe.getRating();
        response.cookTime = recipe.getCookTime();
        response.calories = recipe.getCalories();
        response.description = recipe.getDescription();
        response.urgentMatchedCount = recipe.getUrgentMatchedCount();

        response.availableIngredients = new ArrayList<>(recipe.getAvailableIngredients());
        response.missingIngredients = new ArrayList<>(recipe.getMissingIngredients());

        response.requiredIngredients =
                buildIngredientRows(recipe.getRequiredIngredients(), inventory, foodCatalog);
        response.optionalIngredients =
                buildIngredientRows(recipe.getOptionalIngredients(), inventory, foodCatalog);

        return response;
    }

    /**
     * Builds ingredient rows with inventory status.
     */
    private static List<RecipeIngredientStatusResponse> buildIngredientRows(
            List<Recipe.Ingredient> ingredients,
            List<FoodItem> inventory,
            IFoodCatalog foodCatalog) {

        List<RecipeIngredientStatusResponse> rows = new ArrayList<>();

        for (Recipe.Ingredient ingredient : ingredients) {
            RecipeIngredientStatusResponse row = new RecipeIngredientStatusResponse();
            row.setName(ingredient.getName());
            row.setQuantityText(ingredient.getQuantityText());
            row.setOptional(ingredient.isOptional());

            FoodItem matched =
                    findMatchingInventoryItem(ingredient.getName(), inventory, foodCatalog);

            QuantitySpec recipeQuantity = QuantitySpec.parse(ingredient.getQuantityText());
            row.setInFridge(matched != null);

            if (matched != null) {
                row.setCurrentStockText(matched.getQuantity() + " " + matched.getUnit());
                row.setInventoryCategory(matched.getCategory().getName());

                QuantitySpec stockQuantity =
                        QuantitySpec.of(matched.getQuantity(), matched.getUnit());

                if (isSameUnit(recipeQuantity.getUnit(), stockQuantity.getUnit())
                        && stockQuantity.getAmount() < recipeQuantity.getAmount()) {
                    row.setStatus("partially available");
                    row.setShortageText(formatAmount(
                            recipeQuantity.getAmount() - stockQuantity.getAmount(),
                            recipeQuantity.getUnit()));
                } else {
                    row.setStatus("from current fridge");
                }
            } else if (ingredient.isOptional()) {
                row.setStatus("optional");
                row.setShortageText(ingredient.getQuantityText());
            } else {
                row.setStatus("need to buy");
                row.setShortageText(ingredient.getQuantityText());
            }

            rows.add(row);
        }

        return rows;
    }

    /**
     * Finds the matching inventory item for one ingredient name.
     */
    private static FoodItem findMatchingInventoryItem(
            String ingredientName,
            List<FoodItem> inventory,
            IFoodCatalog foodCatalog) {

        String target = normalize(foodCatalog.canonicalFoodName(ingredientName));

        for (FoodItem item : inventory) {
            String itemName = normalize(foodCatalog.canonicalFoodName(item.getName()));
            if (target.equals(itemName)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns true if two units should be treated as the same unit.
     */
    private static boolean isSameUnit(String left, String right) {
        String unitA = canonicalUnit(left);
        String unitB = canonicalUnit(right);
        return !unitA.isEmpty() && unitA.equals(unitB);
    }

    /**
     * Normalizes unit labels for comparison.
     */
    private static String canonicalUnit(String raw) {
        String unit = normalize(raw);

        if (unit.equals("count") || unit.equals("counts")
                || unit.equals("pc") || unit.equals("pcs")) {
            return "count";
        }

        if (unit.equals("clove") || unit.equals("cloves")) {
            return "clove";
        }

        return unit;
    }

    /**
     * Formats amount text for display.
     */
    private static String formatAmount(double amount, String unit) {
        double rounded = Math.round(amount * 100.0) / 100.0;

        if (Math.abs(rounded - Math.rint(rounded)) < 0.0001) {
            return ((int) Math.rint(rounded)) + " " + unit;
        }

        return rounded + " " + unit;
    }

    /**
     * Normalizes text for comparison.
     */
    private static String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Simple helper for amount and unit.
     */
    private static final class QuantitySpec {
        private final double amount;
        private final String unit;

        private QuantitySpec(double amount, String unit) {
            this.amount = amount;
            this.unit = unit;
        }

        public double getAmount() {
            return amount;
        }

        public String getUnit() {
            return unit;
        }

        /**
         * Parses quantity text into amount and unit.
         * Falls back to 1 count if parsing fails.
         */
        public static QuantitySpec parse(String quantityText) {
            if (quantityText == null || quantityText.isBlank()) {
                return new QuantitySpec(1, "count");
            }

            String[] parts = quantityText.trim().split("\\s+", 2);
            double amount = 1;

            try {
                amount = Double.parseDouble(parts[0]);
            } catch (NumberFormatException ignored) {
                amount = 1;
            }

            String unit;
            if (parts.length > 1) {
                unit = parts[1];
            } else {
                unit = "count";
            }

            return new QuantitySpec(amount, unit);
        }

        /**
         * Creates a QuantitySpec from inventory quantity and unit.
         */
        public static QuantitySpec of(int amount, String unit) {
            if (unit == null || unit.isBlank()) {
                return new QuantitySpec(amount, "count");
            }
            return new QuantitySpec(amount, unit);
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public int getMatchPercent() {
        return matchPercent;
    }

    public double getRating() {
        return rating;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCalories() {
        return calories;
    }

    public String getDescription() {
        return description;
    }

    public int getUrgentMatchedCount() {
        return urgentMatchedCount;
    }

    public List<String> getAvailableIngredients() {
        return new ArrayList<>(availableIngredients);
    }

    public List<String> getMissingIngredients() {
        return new ArrayList<>(missingIngredients);
    }

    public List<RecipeIngredientStatusResponse> getRequiredIngredients() {
        return new ArrayList<>(requiredIngredients);
    }

    public List<RecipeIngredientStatusResponse> getOptionalIngredients() {
        return new ArrayList<>(optionalIngredients);
    }
}
