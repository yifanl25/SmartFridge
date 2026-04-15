package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFoodCategory {
    @Test
    void testConstructorAndGetters() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        assertEquals("c1", category.getId());
        assertEquals("Dairy", category.getName());
        assertEquals("milk", category.getIcon());
    }
}
