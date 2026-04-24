package controller;

import model.FoodCategory;
import model.GroceryItem;
import service.GroceryService;
import service.IGroceryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：GroceryController delegation tests.（ Maps to: {@link GroceryController} → {@link IGroceryService}）。
 */
public class TestGroceryController {
    private GroceryController groceryController;
    private IGroceryService groceryService;
    private FoodCategory cat;

    @BeforeEach
    void setUp() {
        cat = new FoodCategory("c1", "Dairy", "milk");
        groceryService = new GroceryService(List.of(new GroceryItem("1", "Milk", cat, 2, 3.0, false)));
        groceryController = new GroceryController(groceryService);
    }

    /**
     * Test: getItems() delegates to service and returns current list.
     */
    @Test
    void testGetItemsReturnCurrentList() {
        assertEquals(1,groceryController.getItems().size());
    }

    /**
     * Test: addLine() delegates to service and increases list size.
     */
    @Test
    void testAddLineDelegatesToGroceryService() {
        FoodCategory cat2 = new FoodCategory("c2", "Produce", "leaf");
        groceryController.addLine(new GroceryItem("2", "Spinach", cat2, 1, 1.5, false));
        assertEquals(2, groceryController.getItems().size());
    }

    /**
     * Tests filterByCategory() delegates to service and returns matching items.
     */
    @Test
    void testFilterByGroceryDelegatesToGroceryService() {
        List<GroceryItem> result = groceryController.filterByCategory("Dairy");
        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getName());
    }

    /**
     * Tests filterByCategory() returns empty list for non-matching category.
     */
    @Test
    void testFilterByCategoryReturnsEmptyForNonMatchingCategory() {
        List<GroceryItem> result = groceryController.filterByCategory("Produce");
        assertEquals(0, result.size());
    }

    /**
     * Tests ToggleCollected delegates to service and the updated state is reflected.
     * <p>
     * Maps to: {@link GroceryController#toggleCollected(String)} → {@link GroceryService#toggleCollected(String)}
     */
    @Test
    void testToggleCollectedDelegatesToGroceryService() {
        assertEquals(true, groceryController.toggleCollected("1").isCollected());
    }

    /**
     * Tests UpdateQuantity delegates to service and the quantity is correct.
     * <p>
     * Maps to: {@link GroceryController#updateQuantity(String, int)} → {@link GroceryService#updateQuantity(String, int)}
     */
    @Test
    void testUpdateQuantityDelegatesToGroceryService() {
        assertEquals(3, groceryController.updateQuantity("1", 1).getQuantity());
    }

    /**
     * Tests DeleteItem delegates to service and service list is cleared.
     * <p>
     * Maps to: {@link GroceryController#deleteItem(String)} → {@link GroceryService#deleteItem(String)}
     */
    @Test
    void testDeleteItemDelegatesToGroceryService() {
        groceryController.deleteItem("1");
        assertEquals(0, groceryService.getItems().size());
    }

    /**
     * Tests CalculateSubtotal(only checked items) delegates to service and result matches PRD example.
     * <p>
     * Maps to: {@link GroceryController#calculateSubtotal()} → {@link GroceryService#calculateSubtotal()}
     */
    @Test
    void testCalculateSubtotalDelegatesToGroceryService() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        IGroceryService svc = new GroceryService(List.of(new GroceryItem("1", "Milk", cat, 2, 3.0, true)));
        GroceryController ctrl = new GroceryController(svc);
        assertEquals(6.0, ctrl.calculateSubtotal());
    }

    /**
     * Tests CalculateTax delegates to service based on 8% tax rate.
     * <p>
     * Maps to: {@link GroceryController#calculateTax(double)} → {@link GroceryService#calculateTax(double)}
     */
    @Test
    void testCalculateTaxDelegatesToGroceryService() {
        assertEquals(0.48, groceryController.calculateTax(6.0), 0.0001);
    }

    /**
     * Tests calculateTotal delegates to service and calculate correctly.
     * <p>
     * Maps to: {@link GroceryController#calculateTotal(double, double)} → {@link GroceryService#calculateTotal(double, double)}
     */
    @Test
    void testCalculateTotalDelegatesToGroceryService() {
        assertEquals(6.48, groceryController.calculateTotal(6.0, 0.48), 0.0001);
    }

    /**
     * Test Checkout delegates to service and ensures grocery list is cleared.
     * <p>
     * Maps to: {@link GroceryController#checkout()} → {@link GroceryService#checkout()}
     */
    @Test
    void testCheckoutDelegatesToGroceryService() {
        groceryController.checkout();
        assertEquals(0, groceryService.getItems().size());
    }
}
