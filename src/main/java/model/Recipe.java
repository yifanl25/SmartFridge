package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe aggregate: static fields from JSON plus runtime-computed match score and ingredient availability.
 * {@link HealthTag} and {@link Ingredient} are static nested types to keep recipe vocabulary in one place.
 * <p>
 * 菜谱聚合：来自 JSON 的静态字段 + 运行时计算的匹配分与食材可用性。
 * {@link HealthTag} 与 {@link Ingredient} 为静态嵌套类型，便于将菜谱相关类型收敛在同一源文件。
 */
public class Recipe {

    /**
     * Tags used for preference-alignment scoring (not the same as {@link HealthGoal} on {@link Preference}).
     * <p>
     * 用于「偏好对齐」打分的标签（与用户偏好中的 {@link HealthGoal} 不是同一概念）。
     */
    public enum HealthTag {
        /** High protein positioning. / 高蛋白定位。 */
        HIGH_PROTEIN,
        /** Low calorie positioning. / 低卡定位。 */
        LOW_CALORIE,
        /** Blood-sugar friendly positioning. / 控糖友好定位。 */
        BLOOD_SUGAR_FRIENDLY,
        /** Neutral / balanced fallback alignment. / 中性/平衡型回落对齐。 */
        BALANCED
    }

    /**
     * One ingredient line on a recipe; lines with {@code optional==true} are excluded from main coverage score.
     * <p>
     * 菜谱中的一行配料；{@code optional==true} 的行不计入主匹配分覆盖率。
     */
    public static final class Ingredient {
        private final String name;
        private final String quantityText;
        private final boolean optional;

        /**
         * @param name         ingredient name / 食材名
         * @param quantityText human-readable quantity / 人类可读用量文案
         * @param optional     whether excluded from required coverage / 是否从必选覆盖中排除
         */
        public Ingredient(String name, String quantityText, boolean optional) {
            this.name = name;
            this.quantityText = quantityText;
            this.optional = optional;
        }

        /** Returns ingredient name. / 返回食材名。 */
        public String getName() {
            return name;
        }

        /** Returns quantity text. / 返回用量文案。 */
        public String getQuantityText() {
            return quantityText;
        }

        /** Returns whether this line is optional for scoring. / 是否可选（不参与主分覆盖）。 */
        public boolean isOptional() {
            return optional;
        }
    }

    private final String id;
    private final String title;
    private final RecipeCategory recipeCategory;
    private final List<HealthTag> healthTags;
    private final List<Ingredient> requiredIngredients;
    private final List<Ingredient> optionalIngredients;
    private final double matchScore;
    private final double rating;
    private final int cookTime;
    private final int calories;
    private final String description;
    private final List<String> availableIngredients;
    private final List<String> missingIngredients;
    /** PRD tie-break: required ingredients matched by urgent inventory items. / PRD 决胜：由临期库存匹配到的必选食材数。 */
    private final int urgentMatchedCount;

    /**
     * Full constructor including computed fields (used internally and by {@link #withComputed}).
     * <p>
     * 包含计算字段的完整构造（内部及 {@link #withComputed} 使用）。
     */
    public Recipe(
            String id,
            String title,
            RecipeCategory recipeCategory,
            List<HealthTag> healthTags,
            List<Ingredient> requiredIngredients,
            List<Ingredient> optionalIngredients,
            double matchScore,
            double rating,
            int cookTime,
            int calories,
            String description,
            List<String> availableIngredients,
            List<String> missingIngredients,
            int urgentMatchedCount) {
        this.id = id;
        this.title = title;
        this.recipeCategory = recipeCategory;
        this.healthTags = new ArrayList<>(healthTags);
        this.requiredIngredients = new ArrayList<>(requiredIngredients);
        this.optionalIngredients = new ArrayList<>(optionalIngredients);
        this.matchScore = matchScore;
        this.rating = rating;
        this.cookTime = cookTime;
        this.calories = calories;
        this.description = description;
        this.availableIngredients = new ArrayList<>(availableIngredients);
        this.missingIngredients = new ArrayList<>(missingIngredients);
        this.urgentMatchedCount = urgentMatchedCount;
    }

    /**
     * Factory for JSON-loaded templates before scoring (match score 0, empty availability lists).
     * <p>
     * 打分前的 JSON 模板工厂（匹配分为 0，可用/缺失列表为空）。
     */
    public static Recipe loaded(
            String id,
            String title,
            RecipeCategory recipeCategory,
            List<HealthTag> healthTags,
            List<Ingredient> requiredIngredients,
            List<Ingredient> optionalIngredients,
            double rating,
            int cookTime,
            int calories,
            String description) {
        return new Recipe(
                id,
                title,
                recipeCategory,
                healthTags,
                requiredIngredients,
                optionalIngredients,
                0.0,
                rating,
                cookTime,
                calories,
                description,
                List.of(),
                List.of(),
                0);
    }

    /**
     * Returns a copy with computed match fields (immutable outer recipe).
     * <p>
     * 返回带计算匹配字段的拷贝（外层菜谱仍不可变语义）。
     */
    public Recipe withComputed(
            double matchScore,
            List<String> availableIngredients,
            List<String> missingIngredients,
            int urgentMatchedCount) {
        return new Recipe(
                id,
                title,
                recipeCategory,
                healthTags,
                requiredIngredients,
                optionalIngredients,
                matchScore,
                rating,
                cookTime,
                calories,
                description,
                availableIngredients,
                missingIngredients,
                urgentMatchedCount);
    }

    /** Returns recipe id. / 返回菜谱 id。 */
    public String getId() {
        return id;
    }

    /** Returns title. / 返回标题。 */
    public String getTitle() {
        return title;
    }

    /** Returns recipe category (not food category). / 返回菜谱分类（非食材分类）。 */
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /** Returns a defensive copy of health tags. / 返回健康标签的防御性拷贝。 */
    public List<HealthTag> getHealthTags() {
        return new ArrayList<>(healthTags);
    }

    /** Returns a defensive copy of required ingredient lines. / 返回必选配料行的防御性拷贝。 */
    public List<Ingredient> getRequiredIngredients() {
        return new ArrayList<>(requiredIngredients);
    }

    /** Returns a defensive copy of optional ingredient lines. / 返回可选配料行的防御性拷贝。 */
    public List<Ingredient> getOptionalIngredients() {
        return new ArrayList<>(optionalIngredients);
    }

    /** Returns computed match score (0 before scoring). / 返回计算后的匹配分（打分前为 0）。 */
    public double getMatchScore() {
        return matchScore;
    }

    /** Returns star-style rating from JSON. / 返回 JSON 中的星级类评分。 */
    public double getRating() {
        return rating;
    }

    /** Returns cook time in minutes. / 返回烹饪时间（分钟）。 */
    public int getCookTime() {
        return cookTime;
    }

    /** Returns calories estimate. / 返回热量估算。 */
    public int getCalories() {
        return calories;
    }

    /** Returns description text. / 返回描述文案。 */
    public String getDescription() {
        return description;
    }

    /** Returns matched required ingredient names (runtime). / 返回已匹配的必选食材名（运行时）。 */
    public List<String> getAvailableIngredients() {
        return new ArrayList<>(availableIngredients);
    }

    /** Returns missing required ingredient names (runtime). / 返回缺失的必选食材名（运行时）。 */
    public List<String> getMissingIngredients() {
        return new ArrayList<>(missingIngredients);
    }

    /** Returns urgent-match count for tie-break. / 返回临期匹配计数用于决胜。 */
    public int getUrgentMatchedCount() {
        return urgentMatchedCount;
    }
}
