package API.dto;

import api.dto.FoodItemResponse;
import model.FoodCategory;
import model.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FoodItemResponse DTO.
 * Verifies that from() correctly maps all fields from FoodItem.
 */
public class TestFoodItemResponse {

    private FoodCategory category;
    private String today;

    @BeforeEach
    void setUp() {
        category = new FoodCategory("c1", "Dairy", "milk");
        today = LocalDate.now().toString();
    }

    /**
     * Tests that from() maps all basic fields correctly.
     */
    @Test
    void testFromMapsAllFieldsCorrectly() {
        String expiry = LocalDate.now().plusDays(7).toString();
        FoodItem item = new FoodItem("f1", "Milk", category, 2, "pcs", today, expiry);
        FoodItemResponse response = FoodItemResponse.from(item);

        assertEquals("f1",    response.getId());
        assertEquals("Milk",  response.getName());
        assertEquals(category, response.getCategory());
        assertEquals(2,       response.getQuantity());
        assertEquals("pcs",   response.getUnit());
        assertEquals(today,   response.getCreatedAt());
        assertEquals(expiry,  response.getExpiryDate());
    }

    /**
     * Tests that isNewItem() returns true when item was created today.
     */
    @Test
    void testIsNewItemReturnsTrueWhenCreatedToday() {
        FoodItem item = new FoodItem(
                "f2", "Milk", category, 1, "pcs",
                today, LocalDate.now().plusDays(7).toString());
        FoodItemResponse response = FoodItemResponse.from(item);
        assertTrue(response.isNewItem());
    }

    /**
     * Tests that isNewItem() returns false when item was created more than 1 day ago.
     */
    @Test
    void testIsNewItemReturnsFalseWhenCreatedTwoDaysAgo() {
        String twoDaysAgo = LocalDate.now().minusDays(2).toString();
        FoodItem item = new FoodItem(
                "f3", "Milk", category, 1, "pcs",
                twoDaysAgo, LocalDate.now().plusDays(7).toString());
        FoodItemResponse response = FoodItemResponse.from(item);
        assertFalse(response.isNewItem());
    }

    /**
     * Tests that isUrgent() returns true when item expires today.
     */
    @Test
    void testIsUrgentReturnsTrueWhenExpiryIsToday() {
        FoodItem item = new FoodItem("f4", "Milk", category, 1, "pcs", today, today);
        FoodItemResponse response = FoodItemResponse.from(item);
        assertTrue(response.isUrgent());
    }

    /**
     * Tests that isUrgent() returns false when item expires in the future.
     */
    @Test
    void testIsUrgentReturnsFalseWhenExpiryIsFuture() {
        String future = LocalDate.now().plusDays(3).toString();
        FoodItem item = new FoodItem("f5", "Milk", category, 1, "pcs", today, future);
        FoodItemResponse response = FoodItemResponse.from(item);
        assertFalse(response.isUrgent());
    }
}