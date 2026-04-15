package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInventoryService {
    private InventoryService inventoryService;
    private IFoodCatalog foodCatalog;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)));
        inventoryService = new InventoryService(foodCatalog);
    }

    @Test void testGetAllItemsReturnsCurrentInventory() { assertEquals(0, inventoryService.getAllItems().size()); }
    @Test void testAddItemCreatesFoodItemFromCatalog() { assertEquals("Milk", inventoryService.addItem("Milk").getName()); }
    @Test void testFilterByCategoryReturnsMatchingItems() { inventoryService.addItem("Milk"); assertEquals(1, inventoryService.filterByCategory("Dairy").size()); }
    @Test void testSortByExpiryOrdersUrgentFirst() { inventoryService.addItem("Milk"); assertEquals(1, inventoryService.sortByExpiry().size()); }
    @Test void testSortByCreatedTimeOrdersNewestFirst() { inventoryService.addItem("Milk"); assertEquals(1, inventoryService.sortByCreatedTime().size()); }
    @Test void testClearInventoryRemovesAllItems() { inventoryService.addItem("Milk"); inventoryService.clearInventory(); assertEquals(0, inventoryService.getAllItems().size()); }
}
