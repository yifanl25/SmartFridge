package API.dto;


import api.dto.GroceryAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for GroceryAddRequest DTO.
 * Verifies default values, setters, and getters behave correctly.
 */

public class TestGroceryAddRequest {
    private GroceryAddRequest request;
    @BeforeEach
    void setup() {
        request = new GroceryAddRequest();
    }

    @Test
    void testSetAndGetFoodName() {
        request.setFoodName("Milk");
        assertEquals("Milk", request.getFoodName());
    }

    @Test
    void testSetAndGetQuantity() {
        request.setQuantity(2);
        assertEquals(2, request.getQuantity());
    }

    @Test
    void testSetAndGetPrice() {
        request.setPrice(2.01);
        assertEquals(2.01, request.getPrice());
    }
}
