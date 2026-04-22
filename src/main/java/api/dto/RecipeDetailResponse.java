package api.dto;

import model.FoodItem;
import model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * 这是给 recipe detail page 用的响应 DTO。
 *
 * 大白话：
 * 前端点开某一道菜的时候，
 * 它想一次拿到：
 * - 菜谱本身信息
 * - 匹配率
 * - 哪些食材已经有
 * - 哪些食材还缺
 * - 每一条 ingredient 的库存状态
 *
 * 这些信息就是由这个类打包出来的。
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
     * 把你系统里的 Recipe + 当前库存，转成前端能直接吃的 detail response。
     *
     * 这个静态工厂方法就是整个 detail 组装的入口。
     */
    // ===== teammate note =====
    // 这个工厂方法负责把 Recipe 变成前端可直接消费的 detail response。
    // 如果前端 detail page 还想加字段，优先改这里，不要让 controller 自己拼字段。
    // insert your code here: extend response mapping carefully
    public static RecipeDetailResponse from(
            Recipe recipe,
            List<FoodItem> inventory,
            Function<String, String> canonicalNameResolver) {
        RecipeDetailResponse r = new RecipeDetailResponse();
        r.id = recipe.getId();
        r.title = recipe.getTitle();
        r.category = recipe.getRecipeCategory().getName();
        r.matchScore = recipe.getMatchScore();

        // 匹配率 = 已满足的 required ingredients / 全部 required ingredients。
        int totalRequired = recipe.getRequiredIngredients().size();
        int matchedRequired = recipe.getAvailableIngredients().size();
        r.matchPercent = totalRequired == 0 ? 0 : (int) Math.round((double) matchedRequired * 100.0 / totalRequired);

        r.rating = recipe.getRating();
        r.cookTime = recipe.getCookTime();
        r.calories = recipe.getCalories();
        r.description = recipe.getDescription();
        r.urgentMatchedCount = recipe.getUrgentMatchedCount();

        // 这两个 list 是现成结果，直接拷一份给 response。
        r.availableIngredients = new ArrayList<>(recipe.getAvailableIngredients());
        r.missingIngredients = new ArrayList<>(recipe.getMissingIngredients());

        // required 和 optional 分开组装，前端展示会更清楚。
        r.requiredIngredients = buildIngredientRows(recipe.getRequiredIngredients(), inventory, canonicalNameResolver);
        r.optionalIngredients = buildIngredientRows(recipe.getOptionalIngredients(), inventory, canonicalNameResolver);
        return r;
    }

    /**
     * 把一组 ingredient 转成“带库存状态说明”的 response 行。
     *
     * 这部分就是从你组员的 recipe-detail 逻辑里抽出来，
     * 但改成适合你当前模型的版本。
     */
    private static List<RecipeIngredientStatusResponse> buildIngredientRows(
            List<Recipe.Ingredient> ingredients,
            List<FoodItem> inventory,
            Function<String, String> canonicalNameResolver) {
        List<RecipeIngredientStatusResponse> rows = new ArrayList<>();
        for (Recipe.Ingredient ingredient : ingredients) {
            RecipeIngredientStatusResponse row = new RecipeIngredientStatusResponse();
            row.setName(ingredient.getName());
            row.setQuantityText(ingredient.getQuantityText());
            row.setOptional(ingredient.isOptional());

            // 先在当前库存里找有没有“对应的同一种食材”。
            FoodItem matched = findMatchingInventoryItem(ingredient.getName(), inventory, canonicalNameResolver);

            // 把 recipe 上写的数量文本尽量解析出来，
            // 后面才能判断是“够用 / 不够用 / 完全没有”。
            QuantitySpec recipeQty = QuantitySpec.parse(ingredient.getQuantityText());
            row.setInFridge(matched != null);

            if (matched != null) {
                // 找到了库存项，先把当前库存信息写回去。
                row.setCurrentStockText(matched.getQuantity() + " " + matched.getUnit());
                row.setInventoryCategory(matched.getCategory().getName());

                QuantitySpec stockQty = QuantitySpec.of(matched.getQuantity(), matched.getUnit());

                // 只有当单位一致，而且库存数量比菜谱需要量少时，
                // 才认定为 partially available。
                if (isSameUnit(recipeQty.unit(), stockQty.unit()) && stockQty.amount() < recipeQty.amount()) {
                    row.setStatus("partially available");
                    row.setShortageText(formatAmount(recipeQty.amount() - stockQty.amount(), recipeQty.unit()));
                } else {
                    row.setStatus("from current fridge");
                }
            } else if (ingredient.isOptional()) {
                // optional ingredient 缺了也没关系，状态写 optional。
                row.setStatus("optional");
                row.setShortageText(ingredient.getQuantityText());
            } else {
                // required ingredient 不在库存里，就是真缺，要买。
                row.setStatus("need to buy");
                row.setShortageText(ingredient.getQuantityText());
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 在库存里找和 ingredient 对应的食材。
     *
     * 这里不是生硬比原始字符串，
     * 而是先走 canonicalFoodName，尽量把别名归一化后再比较。
     */
    private static FoodItem findMatchingInventoryItem(
            String ingredientName,
            List<FoodItem> inventory,
            Function<String, String> canonicalNameResolver) {
        String target = normalize(canonicalNameResolver.apply(ingredientName));
        for (FoodItem item : inventory) {
            String inv = normalize(canonicalNameResolver.apply(item.getName()));
            if (target.equals(inv)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 判断两个单位能不能算“同一种单位”。
     *
     * 比如 count / counts / pc / pcs，
     * 这里都会先归成 count 再比较。
     */
    private static boolean isSameUnit(String a, String b) {
        String left = canonicalUnit(a);
        String right = canonicalUnit(b);
        return !left.isEmpty() && left.equals(right);
    }

    /**
     * 把一些常见单位写法统一一下，避免因为复数或缩写不同而判断失败。
     */
    private static String canonicalUnit(String raw) {
        String u = normalize(raw);
        if (u.equals("count") || u.equals("counts") || u.equals("pc") || u.equals("pcs")) {
            return "count";
        }
        if (u.equals("clove") || u.equals("cloves")) {
            return "clove";
        }
        return u;
    }

    /**
     * 把数量格式化成更好读的文本。
     *
     * 例子：
     * - 2.0 -> 2
     * - 1.25 -> 1.25
     */
    private static String formatAmount(double amount, String unit) {
        double rounded = Math.round(amount * 100.0) / 100.0;
        if (Math.abs(rounded - Math.rint(rounded)) < 0.0001) {
            return ((int) Math.rint(rounded)) + " " + unit;
        }
        return rounded + " " + unit;
    }

    /**
     * 文本统一化工具：去空格 + 转小写。
     */
    private static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * 把“数量 + 单位”临时装在一起，便于比较（Java 11：不用 record）。
     */
    private static final class QuantitySpec {
        private final double amount;
        private final String unit;

        private QuantitySpec(double amount, String unit) {
            this.amount = amount;
            this.unit = unit;
        }

        double amount() {
            return amount;
        }

        String unit() {
            return unit;
        }

        /**
         * 尽量从 quantityText 里解析出数值和单位。
         * 读不出来时，默认按 1 count 处理。
         */
        static QuantitySpec parse(String quantityText) {
            if (quantityText == null || quantityText.isBlank()) {
                return new QuantitySpec(1, "count");
            }
            String[] parts = quantityText.trim().split("\\s+", 2);
            double amt = 1;
            try {
                amt = Double.parseDouble(parts[0]);
            } catch (NumberFormatException ignored) {
                // 读不出数字就继续用默认值 1。
            }
            String u = parts.length > 1 ? parts[1] : "count";
            return new QuantitySpec(amt, u);
        }

        /**
         * 从库存里的 quantity + unit 造一个 QuantitySpec。
         */
        static QuantitySpec of(int amount, String unit) {
            return new QuantitySpec(amount, unit == null || unit.isBlank() ? "count" : unit);
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
