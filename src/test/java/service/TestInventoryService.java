package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：Inventory management's increases and deletes and sorts and finds and changes.（Maps to: {@link InventoryService}）
 */
public class TestInventoryService {
    private InventoryService inventoryService;
    private IFoodCatalog foodCatalog;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)));
        inventoryService = new InventoryService(foodCatalog);
    }

    /**
     * Test: Retrieves all inventory items in the current session.
     * Verification: Initial state should be an empty list.
     * <p>
     * Maps to: {@link InventoryService#getAllItems()}
     */
    @Test
    void testGetAllItemsReturnsCurrentInventory() {
        assertEquals(0, inventoryService.getAllItems().size());
    }

    /**
     * Test：Adds item by name(catalog lookup and defaults applied).
     * Verification: Item is successfully created and matches catalog name.
     * <p>
     * Maps to: {@link InventoryService#addItem(String)}
     */
    @Test
    void testAddItemCreatesFoodItemFromCatalog() {
        assertEquals("Milk", inventoryService.addItem("Milk").getName());
    }

    /**
     * Test: Filters inventory by category ({@link FoodCategory}).
     * Verification: Only items in the specified category are returned.
     * <p>
     * Maps to: {@link InventoryService#filterByCategory(String)}（先 {@link InventoryService#addItem(String)}）
     */
    @Test
    void testFilterByCategoryReturnsMatchingItems() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.filterByCategory("Dairy").size());
    }

    /**
     * Test: Sorts items by expiry date (urgent items first).
     * Verification: Result size matches source (single-item case).
     * <p>
     * Maps to: {@link InventoryService#sortByExpiry()}
     */
    @Test
    void testSortByExpiryOrdersUrgentFirst() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.sortByExpiry().size());
    }

    /**
     * Test: Sorts items by creation time (newest first).
     * Verification: Result is non-empty and size is correct.
     * <p>
     * Maps to: {@link InventoryService#sortByCreatedTime()}
     */
    @Test
    void testSortByCreatedTimeOrdersNewestFirst() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.sortByCreatedTime().size());
    }

    /**
     * Test: Clears all inventory items in the session
     * (used after checkout or session reset as defined in PRD).
     * Verification: {@code getAllItems()} returns 0 after clearing.
     * <p>
     * Maps to: {@link InventoryService#clearInventory()}、{@link InventoryService#getAllItems()}
     */
    @Test
    void testClearInventoryRemovesAllItems() {
        inventoryService.addItem("Milk");
        inventoryService.clearInventory();
        assertEquals(0, inventoryService.getAllItems().size());
    }

    /**
     * Test：Adds all items appended batch {@link FoodItem}.
     * Verification：Total number of items increases accordingly after {@code addAllItems}.
     * <p>
     * Maps to: {@link InventoryService#addAllItems(List)}、{@link InventoryService#getAllItems()}
     */
    @Test
    void testAddAllItemsAppendsBatch() {
        FoodCategory cat = new FoodCategory("c2", "Produce", "leaf");
        FoodItem a = new FoodItem("a1", "Apple", cat, 1, "pcs", "2026-01-01", "2026-02-01");
        FoodItem b = new FoodItem("a2", "Banana", cat, 2, "pcs", "2026-01-01", "2026-02-02");
        inventoryService.addAllItems(List.of(a, b));
        assertEquals(2, inventoryService.getAllItems().size());
    }
}
