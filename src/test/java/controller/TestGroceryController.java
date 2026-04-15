package controller;

import model.FoodCategory;
import model.GroceryItem;
import service.GroceryService;
import service.IGroceryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGroceryController {
    private GroceryController groceryController;
    private IGroceryService groceryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        groceryService = new GroceryService(List.of(new GroceryItem("1", "Milk", cat, 2, 3.0, false)));
        groceryController = new GroceryController(groceryService);
    }

    @Test void testToggleCollectedDelegatesToGroceryService() { assertEquals(true, groceryController.toggleCollected("1").isCollected()); }
    @Test void testUpdateQuantityDelegatesToGroceryService() { assertEquals(3, groceryController.updateQuantity("1", 1).getQuantity()); }
    @Test void testDeleteItemDelegatesToGroceryService() { groceryController.deleteItem("1"); assertEquals(0, groceryService.getItems().size()); }
    @Test void testCalculateSubtotalDelegatesToGroceryService() { assertEquals(6.0, groceryController.calculateSubtotal()); }
    @Test void testCalculateTaxDelegatesToGroceryService() { assertEquals(0.48, groceryController.calculateTax(6.0), 0.0001); }
    @Test void testCalculateTotalDelegatesToGroceryService() { assertEquals(6.48, groceryController.calculateTotal(6.0, 0.48), 0.0001); }
    @Test void testCheckoutDelegatesToGroceryService() { groceryController.checkout(); assertEquals(0, groceryService.getItems().size()); }
}
