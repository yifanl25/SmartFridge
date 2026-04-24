package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：GroceryItem tests.(Maps to: {@link GroceryItem}；price calculations are handled by the service layer).
 */
public class TestGroceryItem {
    /**
     * Test: constructor, getters, and mutable state changes(quantity and collected status).
     * Verification: behavior matches UI interactions such as increment/decrement
     * <p>
     * Maps to: {@link GroceryItem#GroceryItem(String, String, FoodCategory, int, double, boolean)}，
     * {@link GroceryItem#getId()}, {@link GroceryItem#getName()}, {@link GroceryItem#getCategory()}, {@link GroceryItem#getQuantity()},
     * {@link GroceryItem#getPrice()}, {@link GroceryItem#isCollected()}, {@link GroceryItem#setCollected(boolean)}, {@link GroceryItem#setQuantity(int)}
     */
    @Test
    void testConstructorAndStateChanges() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        GroceryItem item = new GroceryItem("g1", "Milk", category, 2, 3.5, false);

        assertEquals("g1", item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(category, item.getCategory());
        assertEquals(2, item.getQuantity());
        assertEquals(3.5, item.getPrice());
        assertFalse(item.isCollected());

        item.setCollected(true);
        item.setQuantity(5);
        assertTrue(item.isCollected());
        assertEquals(5, item.getQuantity());
    }
}
