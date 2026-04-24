package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：Inventory food item.(Maps to: {@link FoodItem}，including "newly added" and "near expiry" rules).
 */
public class TestFoodItem {
    /**
     * Test: Constructor and getter; "new item" flag when created today.
     * Verification：All fields match input and {@link FoodItem#isNew()} returns true。
     * <p>
     * Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，
     * {@link FoodItem#getId()}, {@link FoodItem#getName()}, {@link FoodItem#getCategory()}, {@link FoodItem#getQuantity()},
     * {@link FoodItem#getUnit()}, {@link FoodItem#isNew()}
     */
    @Test
    void testConstructorAndGettersAndNew() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem item = new FoodItem(
                "f1",
                "Milk",
                category,
                1,
                "pcs",
                today,
                LocalDate.now().plusDays(7).toString());

        assertEquals("f1", item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(category, item.getCategory());
        assertEquals(1, item.getQuantity());
        assertEquals("pcs", item.getUnit());
        assertTrue(item.isNew());
    }

    /**
     * Test: "urgent / near expiry" flag when expiry date is today.
     * Verification：{@link FoodItem#isUrgent()} returns true。
     * <p>
     * Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，{@link FoodItem#isUrgent()}
     */
    @Test
    void testIsUrgentWhenExpiryIsToday() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem urgent = new FoodItem("f2", "Milk", category, 1, "pcs", today, today);
        assertTrue(urgent.isUrgent());
    }

    /**
     * Test：Item is not urgent when expiry date is in the future.
     * Verification：{@link FoodItem#isUrgent()} returns false。
     * <p>
     * Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，{@link FoodItem#isUrgent()}
     */
    @Test
    void testIsNotUrgentWhenExpiryIsFuture() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem safe = new FoodItem(
                "f3", "Milk", category, 1, "pcs", today, LocalDate.now().plusDays(3).toString());
        assertFalse(safe.isUrgent());
    }
}
