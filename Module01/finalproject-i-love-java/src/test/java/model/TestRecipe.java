package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：菜谱领域模型与 JSON 模板字段（对应 {@link Recipe}、嵌套 {@link Recipe.HealthTag}/{@link Recipe.Ingredient}）。
 */
public class TestRecipe {
    /**
     * 测试功能：{@link Recipe#loaded(...)} 工厂与访问器。
     * 验证点：静态字段、嵌套类型、计算前 matchScore 为 0、列表初始为空。
     * <p>
     * 对应源码 / Maps to: {@link Recipe#loaded(String, String, RecipeCategory, java.util.List, java.util.List, java.util.List, double, int, int, String)}，
     * {@link Recipe#getId()}, {@link Recipe#getTitle()}, {@link Recipe#getRecipeCategory()}, {@link Recipe#getMatchScore()},
     * {@link Recipe#getRating()}, {@link Recipe#getCookTime()}, {@link Recipe#getCalories()}, {@link Recipe#getDescription()},
     * {@link Recipe#getRequiredIngredients()}, {@link Recipe#getAvailableIngredients()}, {@link Recipe#getMissingIngredients()}
     */
    @Test
    void testLoadedTemplateAndGetters() {
        RecipeCategory category = new RecipeCategory("r1", "Quick", "bolt");
        Recipe recipe = Recipe.loaded(
                "id1",
                "Egg Bowl",
                category,
                List.of(Recipe.HealthTag.HIGH_PROTEIN),
                List.of(new Recipe.Ingredient("Egg", "1 ct", false)),
                List.of(),
                4.6,
                15,
                320,
                "Simple");

        assertEquals("id1", recipe.getId());
        assertEquals("Egg Bowl", recipe.getTitle());
        assertEquals(category, recipe.getRecipeCategory());
        assertEquals(0.0, recipe.getMatchScore());
        assertEquals(4.6, recipe.getRating());
        assertEquals(15, recipe.getCookTime());
        assertEquals(320, recipe.getCalories());
        assertEquals("Simple", recipe.getDescription());
        assertEquals(1, recipe.getRequiredIngredients().size());
        assertEquals(0, recipe.getAvailableIngredients().size());
        assertEquals(0, recipe.getMissingIngredients().size());
    }
}
