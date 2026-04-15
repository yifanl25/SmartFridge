package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGroceryItem {
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
