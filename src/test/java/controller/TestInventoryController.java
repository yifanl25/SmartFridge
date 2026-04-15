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

public class TestInventoryController {
    private InventoryController inventoryController;
    private IInventoryService inventoryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        inventoryService = new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat))));
        inventoryController = new InventoryController(inventoryService);
    }

    @Test void testGetVisibleItemsDelegatesToInventoryService() { assertEquals(0, inventoryController.getVisibleItems().size()); }
    @Test void testAddItemDelegatesToInventoryService() { assertEquals("Milk", inventoryController.addItem("Milk").getName()); }
    @Test void testFilterByCategoryDelegatesToInventoryService() { inventoryController.addItem("Milk"); assertEquals(1, inventoryController.filterByCategory("Dairy").size()); }
    @Test void testSortByExpiryDelegatesToInventoryService() { inventoryController.addItem("Milk"); assertEquals(1, inventoryController.sortByExpiry().size()); }
    @Test void testSortByCreatedTimeDelegatesToInventoryService() { inventoryController.addItem("Milk"); assertEquals(1, inventoryController.sortByCreatedTime().size()); }
}
