package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：FoodCatalogEntry tests.（Maps to: {@link FoodCatalogEntry}，Comes from: {@code food_catalog.json}）。
 */
public class TestFoodCatalogEntry {
    /**
     * Tests: The catalog entry correctly stores the food name, default expiry days, and category.
     * Verification: All fields match the constructor inputs.
     * <p>
     * Maps to: {@link FoodCatalogEntry#FoodCatalogEntry(String, int, FoodCategory)}，
     * {@link FoodCatalogEntry#getFoodName()}，{@link FoodCatalogEntry#getDefaultExpiryDays()}，{@link FoodCatalogEntry#getCategory()}
     */
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalogEntry entry = new FoodCatalogEntry("Milk", 7, category);
        assertEquals("Milk", entry.getFoodName());
        assertEquals(7, entry.getDefaultExpiryDays());
        assertEquals(category, entry.getCategory());
    }
}
