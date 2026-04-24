package API.web;


import api.dto.AddFoodRequest;
import api.dto.FoodItemResponse;
import api.web.InventoryApiController;
import controller.InventoryController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.InventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for InventoryApiController.
 * Directly instantiates the controller without starting a Spring server.
 * Each test targets one specific endpoint or branch.
 */
public class TestInventoryApiController {

    private InventoryApiController inventoryApiController;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalog foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat)));
        inventoryService = new InventoryService(foodCatalog);
        InventoryController inventoryController = new InventoryController(inventoryService);
        inventoryApiController = new InventoryApiController(inventoryController);
    }

    // GET /api/inventory

    /**
     * Tests GET with no filters returns all items.
     * Branch: category == null, sort == null → return full list.
     */
    @Test
    void testListWithNoFiltersReturnsAllItems() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list(null, null);
        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getName());
    }


    /**
     * Tests GET with category filter returns matching items.
     * Branch: category is not blank → call filterByCategory().
     */
    @Test
    void testListWithCategoryFilterReturnsMatchingItems() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list("Dairy", null);
        assertEquals(1, result.size());
    }

    /**
     * Tests GET with non-matching category returns empty list.
     */
    @Test
    void testListWithNonMatchingCategoryReturnsEmptyList() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list("Produce", null);
        assertEquals(0, result.size());
    }

    /**
     * Tests GET with sort=created returns items sorted by created time.
     * Branch: sort == "created" → sort by createdAt reversed.
     */
    @Test
    void testListWithSortCreatedReturnsSortedItems() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list(null, "created");
        assertEquals(1, result.size());
    }

    /**
     * Tests GET with unknown sort value returns unsorted items.
     * Branch: sort is not blank but not "expiry" or "created" → no sort applied.
     */
    @Test
    void testListWithUnknownSortValueReturnsUnsortedItems() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list(null, "unknown");
        assertEquals(1, result.size());
    }

    /**
     * Tests GET with blank sort value returns items without sorting.
     * Branch: sort is blank → skip sort block entirely.
     */
    @Test
    void testListWithBlankSortReturnsItemsWithoutSorting() {
        inventoryApiController.add(makeRequest("Milk"));
        List<FoodItemResponse> result = inventoryApiController.list(null, "   ");
        assertEquals(1, result.size());
    }

    // POST /api/inventory

    /**
     * Tests POST with valid food name returns 200 OK with added item.
     * Branch: body is valid → addItem() → return ok.
     */
    @Test
    void testAddWithValidFoodNameReturnsOk() {
        ResponseEntity<FoodItemResponse> response = inventoryApiController.add(makeRequest("Milk"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Milk", response.getBody().getName());
    }


    /**
     * Tests POST actually adds item to inventory service.
     */
    @Test
    void testAddIncreasesInventorySize() {
        assertEquals(0, inventoryService.getAllItems().size());
        inventoryApiController.add(makeRequest("Milk"));
        assertEquals(1, inventoryService.getAllItems().size());
    }

    // Helper

    /**
     * Creates an AddFoodRequest with the given food name.
     */
    private AddFoodRequest makeRequest(String foodName) {
        AddFoodRequest req = new AddFoodRequest();
        req.setFoodName(foodName);
        return req;
    }
}