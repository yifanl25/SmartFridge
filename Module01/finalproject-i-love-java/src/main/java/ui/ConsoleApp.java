package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import controller.GroceryController;
import controller.InventoryController;
import controller.PreferenceController;
import controller.RecommendationController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;
import model.GroceryItem;
import model.HealthGoal;
import model.Preference;
import model.Recipe;
import service.IFoodCatalog;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

/**
 * Text-mode REPL for the Smart Fridge demo: inventory, preference, recommendations, grocery from missing items,
 * export JSON, checkout with session reset via injected {@link GroceryController} hook.
 * <p>
 * 智能冰箱演示的文本 REPL：库存、偏好、推荐、由缺失食材生成购物、导出 JSON、通过注入的 {@link GroceryController} 钩子结账并重置会话。
 */
public class ConsoleApp {
    /**
     * Placeholder unit price for grocery lines built from ingredient names (catalog has no MSRP in PRD).
     * <p>
     * 由食材名生成购物行时的占位单价（PRD 目录无建议零售价）。
     */
    private static final double DEFAULT_GROCERY_UNIT_PRICE = 2.99;

    private final InventoryController inventoryController;
    private final PreferenceController preferenceController;
    private final RecommendationController recommendationController;
    private final GroceryController groceryController;
    private final IFoodCatalog foodCatalog;

    /**
     * Last printed recommendation ordering (1-based {@code grocery add} indices follow this list).
     * <p>
     * 上次打印的推荐顺序（{@code grocery add} 使用的一维下标与此列表一致）。
     */
    private List<Recipe> lastRecommendations = List.of();

    public ConsoleApp(
            InventoryController inventoryController,
            PreferenceController preferenceController,
            RecommendationController recommendationController,
            GroceryController groceryController,
            IFoodCatalog foodCatalog) {
        this.inventoryController = inventoryController;
        this.preferenceController = preferenceController;
        this.recommendationController = recommendationController;
        this.groceryController = groceryController;
        this.foodCatalog = foodCatalog;
    }

    public void start() {
        System.out.println("Smart Fridge console ready. Call runInteractive(System.in) to use the REPL.");
    }

    public void runInteractive(InputStream input) {
        System.out.println();
        System.out.println("=== Smart Fridge (text mode) ===");
        System.out.println("Demo inventory is preloaded so you can test the full loop immediately.");
        printHelp();
        try (Scanner scanner = new Scanner(input)) {
            while (true) {
                System.out.print("> ");
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine();
                if (shouldQuit(line)) {
                    break;
                }
                try {
                    processCommand(line);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }
            }
        }
        System.out.println("Goodbye.");
    }

    private static boolean shouldQuit(String line) {
        if (line == null) {
            return true;
        }
        String t = line.trim().toLowerCase(Locale.ROOT);
        return t.equals("quit") || t.equals("exit");
    }

    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println("  help");
        System.out.println("  demo                    — print one recommended walkthrough");
        System.out.println("  add <foodName>          — add inventory (catalog-resolved name)");
        System.out.println("  goal <HEALTH_GOAL>      — " + java.util.Arrays.toString(HealthGoal.values()));
        System.out.println("  inventory               — list inventory");
        System.out.println("  preference              — show current health goal");
        System.out.println("  recommend               — score recipes from inventory + preference");
        System.out.println("  detail <n>              — show recommendation #n details");
        System.out.println("  sort score|cook         — sort last recommendation list");
        System.out.println("  filter <text>           — filter recipe category (substring)");
        System.out.println("  grocery add <n>         — add missing ingredients from recommendation #n (1-based)");
        System.out.println("  grocery                 — list grocery rows");
        System.out.println("  collect <id>            — toggle collected for checkout");
        System.out.println("  qty <id> <delta>        — change grocery quantity");
        System.out.println("  totals                  — subtotal / tax / total (collected rows only)");
        System.out.println("  export-grocery <file>   — save grocery list as JSON");
        System.out.println("  checkout                — end session (clears preference, inventory, recs, grocery)");
        System.out.println("  quit");
        System.out.println();
    }

    public void processCommand(String input) {
        if (input == null) {
            return;
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        String lower = trimmed.toLowerCase(Locale.ROOT);
        if (lower.equals("help")) {
            printHelp();
            return;
        }
        if (lower.equals("demo")) {
            printDemoWalkthrough();
            return;
        }
        if (lower.startsWith("add ")) {
            FoodItem added = inventoryController.addItem(trimmed.substring(4).trim());
            System.out.println("Added: " + added.getName() + " (qty " + added.getQuantity() + ")");
            return;
        }
        if (lower.startsWith("goal ")) {
            String raw = trimmed.substring(5).trim().toUpperCase(Locale.ROOT).replace('-', '_');
            preferenceController.savePreference(HealthGoal.valueOf(raw));
            System.out.println("Health goal set to " + raw);
            return;
        }
        if (lower.equals("inventory") || lower.equals("inv")) {
            printInventory();
            return;
        }
        if (lower.equals("preference") || lower.equals("pref")) {
            Preference p = preferenceController.getPreference();
            System.out.println(p == null ? "(no preference yet)" : p.getHealthGoal());
            return;
        }
        if (lower.equals("recommend") || lower.equals("recs")) {
            runRecommendations();
            return;
        }
        if (lower.startsWith("detail ")) {
            int index = Integer.parseInt(trimmed.substring("detail ".length()).trim());
            printRecipeDetail(index);
            return;
        }
        if (lower.startsWith("sort ")) {
            String mode = trimmed.substring(5).trim().toLowerCase(Locale.ROOT);
            if (mode.equals("score") || mode.equals("match")) {
                lastRecommendations = recommendationController.sortByMatchScore();
                printRecipes(lastRecommendations);
            } else if (mode.equals("cook") || mode.equals("time")) {
                lastRecommendations = recommendationController.sortByCookTime();
                printRecipes(lastRecommendations);
            } else {
                System.out.println("Usage: sort score|match|cook|time");
            }
            return;
        }
        if (lower.startsWith("filter ")) {
            String needle = trimmed.substring(7).trim();
            lastRecommendations = recommendationController.filterByRecipeCategory(needle);
            printRecipes(lastRecommendations);
            return;
        }
        if (lower.equals("grocery")) {
            printGrocery();
            return;
        }
        if (lower.startsWith("grocery add ")) {
            String rest = trimmed.substring("grocery add ".length()).trim();
            addGroceryFromRecipe(Integer.parseInt(rest));
            return;
        }
        if (lower.startsWith("collect ")) {
            String id = trimmed.substring("collect ".length()).trim();
            groceryController.toggleCollected(id);
            System.out.println("Toggled collected for " + id);
            return;
        }
        if (lower.startsWith("qty ")) {
            String[] parts = trimmed.split("\\s+");
            if (parts.length < 3) {
                throw new IllegalArgumentException("usage: qty <id> <delta>");
            }
            String id = parts[1];
            int delta = Integer.parseInt(parts[2]);
            groceryController.updateQuantity(id, delta);
            System.out.println("Updated quantity for " + id);
            return;
        }
        if (lower.equals("totals")) {
            printTotals();
            return;
        }
        if (lower.startsWith("export-grocery ")) {
            String path = trimmed.substring("export-grocery ".length()).trim();
            try {
                exportGrocery(path);
            } catch (IOException ex) {
                System.out.println("I/O error: " + ex.getMessage());
            }
            return;
        }
        if (lower.equals("checkout")) {
            groceryController.checkout();
            lastRecommendations = List.of();
            System.out.println("Checkout complete — session cleared (PRD loop reset).");
            return;
        }
        if (lower.equals("noop")) {
            return;
        }
        System.out.println("Unknown command. Type help.");
    }

    private void printDemoWalkthrough() {
        System.out.println("Suggested quick demo sequence:");
        System.out.println("1) preference");
        System.out.println("2) goal MUSCLE_BUILDING");
        System.out.println("3) inventory");
        System.out.println("4) recommend");
        System.out.println("5) detail 1");
        System.out.println("6) grocery add 1");
        System.out.println("7) grocery");
        System.out.println("8) collect <copied-id>");
        System.out.println("9) totals");
        System.out.println("10) checkout");
    }

    private void runRecommendations() {
        List<FoodItem> inv = inventoryController.getVisibleItems();
        Preference pref = preferenceController.getPreference();
        lastRecommendations = recommendationController.getRecommendations(inv, pref);
        printRecipes(lastRecommendations);
    }

    private void printInventory() {
        List<FoodItem> items = inventoryController.getVisibleItems();
        if (items.isEmpty()) {
            System.out.println("(empty inventory)");
            return;
        }
        for (FoodItem i : items) {
            System.out.println("- " + i.getName()
                    + " | qty " + i.getQuantity()
                    + " | expiry " + i.getExpiryDate()
                    + (i.isUrgent() ? " | URGENT" : ""));
        }
    }

    private void printRecipes(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            System.out.println("(no recipes — run recommend first)");
            return;
        }
        int idx = 1;
        for (Recipe r : recipes) {
            System.out.println(idx + ". " + r.getTitle()
                    + " | match " + String.format(Locale.ROOT, "%.1f", r.getMatchScore())
                    + " | " + r.getRecipeCategory().getName()
                    + " | " + r.getCookTime() + "m"
                    + " | missing " + r.getMissingIngredients().size());
            idx++;
        }
    }

    private void printRecipeDetail(int oneBasedIndex) {
        if (lastRecommendations.isEmpty()) {
            System.out.println("Run recommend first.");
            return;
        }
        if (oneBasedIndex < 1 || oneBasedIndex > lastRecommendations.size()) {
            throw new IllegalArgumentException("index must be 1.." + lastRecommendations.size());
        }
        Recipe recipe = lastRecommendations.get(oneBasedIndex - 1);
        System.out.println("Title: " + recipe.getTitle());
        System.out.println("Category: " + recipe.getRecipeCategory().getName());
        System.out.println("Description: " + recipe.getDescription());
        System.out.println("Available: " + recipe.getAvailableIngredients());
        System.out.println("Missing: " + recipe.getMissingIngredients());
        System.out.println("Optional ingredients:");
        for (Recipe.Ingredient ingredient : recipe.getOptionalIngredients()) {
            System.out.println("- " + ingredient.getName() + " | " + ingredient.getQuantityText());
        }
    }

    private void printGrocery() {
        List<GroceryItem> items = groceryController.getItems();
        if (items.isEmpty()) {
            System.out.println("(grocery empty — use grocery add <n> after recommend)");
            return;
        }
        for (GroceryItem g : items) {
            System.out.println("- id=" + g.getId()
                    + " | " + g.getName()
                    + " | qty " + g.getQuantity()
                    + " | $" + String.format(Locale.ROOT, "%.2f", g.getPrice())
                    + " | collected=" + g.isCollected());
        }
    }

    private void printTotals() {
        double sub = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(sub);
        double total = groceryController.calculateTotal(sub, tax);
        System.out.println("Subtotal: " + String.format(Locale.ROOT, "%.2f", sub));
        System.out.println("Tax (8%): " + String.format(Locale.ROOT, "%.2f", tax));
        System.out.println("Total:    " + String.format(Locale.ROOT, "%.2f", total));
    }

    private void addGroceryFromRecipe(int oneBasedIndex) {
        if (lastRecommendations.isEmpty()) {
            System.out.println("Run recommend first.");
            return;
        }
        if (oneBasedIndex < 1 || oneBasedIndex > lastRecommendations.size()) {
            throw new IllegalArgumentException("index must be 1.." + lastRecommendations.size());
        }
        Recipe recipe = lastRecommendations.get(oneBasedIndex - 1);
        List<String> missing = recipe.getMissingIngredients();
        if (missing.isEmpty()) {
            System.out.println("No missing ingredients for that recipe.");
            return;
        }
        int added = 0;
        for (String ing : missing) {
            GroceryItem line = buildGroceryLine(ing);
            groceryController.addLine(line);
            added++;
        }
        System.out.println("Added " + added + " grocery line(s) from \"" + recipe.getTitle() + "\".");
    }

    private GroceryItem buildGroceryLine(String ingredientName) {
        FoodCatalogEntry entry = foodCatalog.resolveEntry(ingredientName)
                .orElseGet(() -> foodCatalog.searchSuggestions(ingredientName).stream().findFirst().orElse(null));
        FoodCategory category = entry != null
                ? entry.getCategory()
                : new FoodCategory("misc", "Misc", "box");
        String displayName = foodCatalog.canonicalFoodName(ingredientName);
        return new GroceryItem(
                UUID.randomUUID().toString(),
                displayName.isEmpty() ? ingredientName : displayName,
                category,
                1,
                DEFAULT_GROCERY_UNIT_PRICE,
                false);
    }

    private void exportGrocery(String path) throws IOException {
        Path p = Paths.get(path);
        List<GroceryItem> items = new ArrayList<>(groceryController.getItems());
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(p.toFile(), items);
        System.out.println("Wrote " + items.size() + " row(s) to " + p.toAbsolutePath());
    }
}
