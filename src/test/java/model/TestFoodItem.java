package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFoodItem {
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        FoodItem item = new FoodItem(
                "f1",
                "Milk",
                category,
                1,
                "pcs",
                LocalDate.now().toString(),
                LocalDate.now().plusDays(1).toString());

        assertEquals("f1", item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(category, item.getCategory());
        assertEquals(1, item.getQuantity());
        assertEquals("pcs", item.getUnit());
        assertTrue(item.isNew());
        assertTrue(item.isUrgent());
    }
}
