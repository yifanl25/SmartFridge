package controller;

import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.IInventoryService;
import service.InventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：InventoryController delegation tests.（Maps to: {@link InventoryController} → {@link IInventoryService}）。
 */
public class TestInventoryController {
    private InventoryController inventoryController;
    private IInventoryService inventoryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        inventoryService = new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat))));
        inventoryController = new InventoryController(inventoryService);
    }

    /**
     * Test: Retrieve all inventory items and the initial status should be empty.
     * <p>
     * Maps to: {@link InventoryController#getVisibleItems()} → {@link InventoryService#getAllItems()}
     */
    @Test
    void testGetVisibleItemsDelegatesToInventoryService() {
        assertEquals(0, inventoryController.getVisibleItems().size());
    }

    /**
     * Test: AddItem and returns item name matches the catalog.
     * <p>
     * Maps to: {@link InventoryController#addItem(String)} → {@link InventoryService#addItem(String)}
     */
    @Test
    void testAddItemDelegatesToInventoryService() {
        assertEquals("Milk", inventoryController.addItem("Milk").getName());
    }

    /**
     * Test: Filter items by catrgory and filtered result size is correct.
     * <p>
     * Maps to: {@link InventoryController#filterByCategory(String)} → {@link InventoryService#filterByCategory(String)}
     */
    @Test
    void testFilterByCategoryDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.filterByCategory("Dairy").size());
    }

    /**
     * Test: Sorts items by expiry date and delegates to service and returns list.
     * <p>
     * Maps to: {@link InventoryController#sortByExpiry()} → {@link InventoryService#sortByExpiry()}
     */
    @Test
    void testSortByExpiryDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.sortByExpiry().size());
    }

    /**
     * Test: Sorts item by Created time and delegates to service and returns list.
     * <p>
     * Maps to: {@link InventoryController#sortByCreatedTime()} → {@link InventoryService#sortByCreatedTime()}
     */
    @Test
    void testSortByCreatedTimeDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.sortByCreatedTime().size());
    }
}
