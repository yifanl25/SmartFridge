package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecipe {
    @Test
    void testConstructorAndGetters() {
        RecipeCategory category = new RecipeCategory("r1", "Quick", "bolt");
        Recipe recipe = new Recipe(
                "id1",
                "Egg Bowl",
                category,
                0.85,
                4.6,
                15,
                320,
                "Simple",
                List.of("Egg"),
                List.of("Rice"));

        assertEquals("id1", recipe.getId());
        assertEquals("Egg Bowl", recipe.getTitle());
        assertEquals(category, recipe.getRecipeCategory());
        assertEquals(0.85, recipe.getMatchScore());
        assertEquals(4.6, recipe.getRating());
        assertEquals(15, recipe.getCookTime());
        assertEquals(320, recipe.getCalories());
        assertEquals("Simple", recipe.getDescription());
        assertEquals(1, recipe.getAvailableIngredients().size());
        assertEquals(1, recipe.getMissingIngredients().size());
    }
}
