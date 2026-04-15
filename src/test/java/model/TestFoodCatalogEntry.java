package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFoodCatalogEntry {
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalogEntry entry = new FoodCatalogEntry("Milk", 7, category);
        assertEquals("Milk", entry.getFoodName());
        assertEquals(7, entry.getDefaultExpiryDays());
        assertEquals(category, entry.getCategory());
    }
}
