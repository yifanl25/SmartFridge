package service;


import model.FoodCategory;
import model.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for InventoryManager (legacy helper class).
 * Covers addFood, sortItemsByUrgency, filterByCategory, and getInventory.
 */
public class TestInventoryManagerService {

    private InventoryManager inventoryManager;

    @BeforeEach
    void setUp() {
        inventoryManager = new InventoryManager();
    }

    /**
     * Tests addFood() adds an item to the inventory.
     */
    @Test
    void testAddFoodIncreasesInventorySize() {
        inventoryManager.addFood("Milk", "2026-05-01", "Dairy");
        assertEquals(1, inventoryManager.getInventory().size());
    }

    /**
     * Tests addFood() stores the correct name and category.
     */
    @Test
    void testAddFoodStoresCorrectNameAndCategory() {
        inventoryManager.addFood("Milk", "2026-05-01", "Dairy");
        FoodItem item = inventoryManager.getInventory().get(0);
        assertEquals("Milk", item.getName());
        assertEquals("Dairy", item.getCategory().getName());
    }

    /**
     * Tests addFood() sets createdAt to today.
     */
    @Test
    void testAddFoodSetsCreatedAtToToday() {
        inventoryManager.addFood("Egg", "2026-05-01", "Protein");
        FoodItem item = inventoryManager.getInventory().get(0);
        assertEquals(LocalDate.now().toString(), item.getCreatedAt());
    }

    /**
     * Tests sortItemsByUrgency() returns items sorted by expiry date ascending.
     */
    @Test
    void testSortItemsByUrgencyReturnsSortedByExpiryAscending() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        FoodItem early  = new FoodItem("i1", "Milk",   cat, 1, "pcs", "2026-01-01", "2026-02-01");
        FoodItem middle = new FoodItem("i2", "Cheese", cat, 1, "pcs", "2026-01-01", "2026-03-01");
        FoodItem late   = new FoodItem("i3", "Butter", cat, 1, "pcs", "2026-01-01", "2026-04-01");

        List<FoodItem> sorted = InventoryManager.sortItemsByUrgency(
                List.of(late, early, middle));

        assertEquals("Milk",   sorted.get(0).getName());
        assertEquals("Cheese", sorted.get(1).getName());
        assertEquals("Butter", sorted.get(2).getName());
    }

    /**
     * Tests sortItemsByUrgency() with empty list returns empty list.
     */
    @Test
    void testSortItemsByUrgencyWithEmptyListReturnsEmpty() {
        List<FoodItem> result = InventoryManager.sortItemsByUrgency(List.of());
        assertEquals(0, result.size());
    }

    /**
     * Tests filterByCategory() returns matching items (case-insensitive).
     */
    @Test
    void testFilterByCategoryReturnsMatchingItems() {
        inventoryManager.addFood("Milk",  "2026-05-01", "Dairy");
        inventoryManager.addFood("Apple", "2026-05-01", "Produce");
        List<FoodItem> result = inventoryManager.filterByCategory("Dairy");
        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getName());
    }

    /**
     * Tests filterByCategory() is case-insensitive.
     */
    @Test
    void testFilterByCategoryIsCaseInsensitive() {
        inventoryManager.addFood("Milk", "2026-05-01", "Dairy");
        assertEquals(1, inventoryManager.filterByCategory("dairy").size());
    }

    /**
     * Tests filterByCategory() returns empty list for non-matching category.
     */
    @Test
    void testFilterByCategoryReturnsEmptyForNonMatchingCategory() {
        inventoryManager.addFood("Milk", "2026-05-01", "Dairy");
        assertEquals(0, inventoryManager.filterByCategory("Produce").size());
    }

    /**
     * Tests getInventory() returns the backing list directly (legacy API).
     */
    @Test
    void testGetInventoryReturnsSameList() {
        inventoryManager.addFood("Milk", "2026-05-01", "Dairy");
        List<FoodItem> inv = inventoryManager.getInventory();
        assertEquals(1, inv.size());
        // Legacy API: caller can mutate
        inv.add(new FoodItem("x", "Extra",
                new FoodCategory("c2", "Misc", "box"), 1, "pcs", "2026-01-01", "2026-02-01"));
        assertEquals(2, inventoryManager.getInventory().size());
    }
}