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
 * Legacy text-mode REPL kept for demo and backward-compatibility use.
 * <p>
 * It exercises the same internal controllers and services as the HTTP backend, but it is not the
 * official frontend. The official product UI lives in {@code hello_flutter}.
 */
public class ConsoleApp {
    /**
     * Placeholder unit price for grocery lines built from ingredient names (catalog has no MSRP in PRD).
     * <p>
     * 由食材名生成购物行时的占位单价（PRD 目录无建议零售价）。
     */
    private static final double DEFAULT_GROCERY_UNIT_PRICE = 2.99;

    // 购物单价写死 2.99，因为 JSON 目录里没有「建议零售价」这个字段 / Price is 2.99 because catalog JSON has no MSRP.
    // 以后 PRD 加了价格，再改这里或让用户输入 / Change when PRD adds real prices or user input.
    // INSERT YOUR CODE HERE

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

    /**
     * @param inventoryController      inventory / 库存控制器
     * @param preferenceController     preference / 偏好控制器
     * @param recommendationController recommendations / 推荐控制器
     * @param groceryController        grocery + checkout hook / 购物与结账钩子
     * @param foodCatalog              catalog for resolving grocery lines / 用于解析购物行的目录
     */
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

    /**
     * Non-blocking startup message for tests (does not read stdin).
     * <p>
     * 非阻塞启动提示，供测试使用（不读标准输入）。
     */
    public void start() {
        System.out.println("Smart Fridge console ready. Call runInteractive(System.in) to use the REPL.");
    }

    /**
     * Reads commands from {@code input} until quit/exit or EOF.
     * <p>
     * 从 {@code input} 读取命令直至 quit/exit 或 EOF。
     */
    public void runInteractive(InputStream input) {
        System.out.println();
        System.out.println("=== Smart Fridge (text mode) ===");
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

    /**
     * True for null, {@code quit}, or {@code exit} (case-insensitive trim).
     * <p>
     * 对 null、{@code quit}、{@code exit}（忽略大小写 trim）返回 true。
     */
    private static boolean shouldQuit(String line) {
        if (line == null) {
            return true;
        }
        String t = line.trim().toLowerCase(Locale.ROOT);
        return t.equals("quit") || t.equals("exit");
    }

    /**
     * Prints built-in command help to stdout.
     * <p>
     * 向标准输出打印内置命令帮助。
     */
    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println("  help");
        System.out.println("  add <foodName>          — add inventory (catalog-resolved name)");
        System.out.println("  goal <HEALTH_GOAL>      — " + java.util.Arrays.toString(HealthGoal.values()));
        System.out.println("  inventory               — list inventory");
        System.out.println("  preference              — show current health goal");
        System.out.println("  recommend               — score recipes from inventory + preference");
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

    /**
     * Parses one line and dispatches to controllers (also used from unit tests).
     * <p>
     * 解析一行并分派到各控制器（单测亦调用）。
     */
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

    /**
     * Refreshes {@link #lastRecommendations} from current inventory + preference and prints them.
     * <p>
     * 根据当前库存与偏好刷新 {@link #lastRecommendations} 并打印。
     */
    private void runRecommendations() {
        List<FoodItem> inv = inventoryController.getVisibleItems();
        Preference pref = preferenceController.getPreference();
        lastRecommendations = recommendationController.getRecommendations(inv, pref);
        printRecipes(lastRecommendations);
    }

    /** Prints inventory lines to stdout. / 打印库存行。 */
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

    /**
     * Prints numbered recipe summary lines.
     * <p>
     * 打印带序号的菜谱摘要行。
     */
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

    /** Prints grocery rows to stdout. / 打印购物行。 */
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

    /**
     * Prints subtotal, 8% tax, and total from {@link GroceryController} pricing helpers.
     * <p>
     * 通过 {@link GroceryController} 计价辅助方法打印小计、8% 税与总额。
     */
    private void printTotals() {
        double sub = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(sub);
        double total = groceryController.calculateTotal(sub, tax);
        System.out.println("Subtotal: " + String.format(Locale.ROOT, "%.2f", sub));
        System.out.println("Tax (8%): " + String.format(Locale.ROOT, "%.2f", tax));
        System.out.println("Total:    " + String.format(Locale.ROOT, "%.2f", total));
    }

    /**
     * Appends grocery lines for each missing required ingredient of recipe index {@code oneBasedIndex}.
     * <p>
     * 为菜谱序号 {@code oneBasedIndex}（从 1 起）的每条缺失必选食材追加购物行。
     */
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

    /**
     * Builds one {@link GroceryItem} row for a missing ingredient name using catalog category when possible.
     * <p>
     * 为缺失食材名构造一条 {@link GroceryItem}；尽可能使用目录中的分类。
     */
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

    /**
     * Writes current grocery list as pretty JSON to {@code path}.
     * <p>
     * 将当前购物列表以格式化 JSON 写入 {@code path}。
     */
    private void exportGrocery(String path) throws IOException {
        // 作业说：做好的清单要存成 json、xml 或 csv 里的一种。这里<strong>只</strong>写了购物单 JSON。
        // Course: save a list as json OR xml OR csv — today only grocery JSON. 别的清单、别的格式：PRD 写了再加 / Add more if PRD asks.
        // INSERT YOUR CODE HERE
        Path p = Paths.get(path);
        List<GroceryItem> items = new ArrayList<>(groceryController.getItems());
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(p.toFile(), items);
        System.out.println("Wrote " + items.size() + " row(s) to " + p.toAbsolutePath());
    }
}
