package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecipeCategory {
    @Test
    void testConstructorAndGetters() {
        RecipeCategory category = new RecipeCategory("r1", "Quick", "bolt");
        assertEquals("r1", category.getId());
        assertEquals("Quick", category.getName());
        assertEquals("bolt", category.getIcon());
    }
}
