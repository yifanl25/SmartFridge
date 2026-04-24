package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：FoodCategory tests.（Maps to: {@link FoodCategory}，
 * used for inventory and grocery management, not for recipe filtering).
 */
public class TestFoodCategory {
    /**
     * Tests: the food category id, name, and icon in catalog.
     * Verification: Values match the expected static catalog data.
     * <p>
     * Maps to: {@link FoodCategory#FoodCategory(String, String, String)}，
     * {@link FoodCategory#getId()}，{@link FoodCategory#getName()}，{@link FoodCategory#getIcon()}
     */
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        assertEquals("c1", category.getId());
        assertEquals("Dairy", category.getName());
        assertEquals("milk", category.getIcon());
    }
}
