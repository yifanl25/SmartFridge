package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：Recipe category test. (Maps to: {@link RecipeCategory}，separated from food categories{@link FoodCategory}).
 */
public class TestRecipeCategory {
    /**
     * Test: recipe grouping label id, name, and icon.
     * Verification: immutable fields are correctly returned via getters.
     * <p>
     * Maps to: {@link RecipeCategory#RecipeCategory(String, String, String)}，
     * {@link RecipeCategory#getId()}，{@link RecipeCategory#getName()}，{@link RecipeCategory#getIcon()}
     */
    @Test
    void testConstructorAndGetters() {
        RecipeCategory category = new RecipeCategory("r1", "Quick", "bolt");
        assertEquals("r1", category.getId());
        assertEquals("Quick", category.getName());
        assertEquals("bolt", category.getIcon());
    }
}
