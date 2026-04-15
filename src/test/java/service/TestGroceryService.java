package service;

import model.FoodCategory;
import model.GroceryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGroceryService {
    private GroceryService groceryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        groceryService = new GroceryService(List.of(
                new GroceryItem("1", "Milk", cat, 2, 3.0, false)));
    }

    @Test void testGetItemsReturnsCurrentGroceryItems() { assertEquals(1, groceryService.getItems().size()); }
    @Test void testToggleCollectedUpdatesState() { assertEquals(true, groceryService.toggleCollected("1").isCollected()); }
    @Test void testUpdateQuantityChangesAmount() { assertEquals(3, groceryService.updateQuantity("1", 1).getQuantity()); }
    @Test void testDeleteItemRemovesItem() { groceryService.deleteItem("1"); assertEquals(0, groceryService.getItems().size()); }
    @Test void testCalculateSubtotalUsesCollectedItems() { assertEquals(6.0, groceryService.calculateSubtotal()); }
    @Test void testCalculateTaxUsesEightPercent() { assertEquals(0.48, groceryService.calculateTax(6.0), 0.0001); }
    @Test void testCalculateTotalAddsSubtotalAndTax() { assertEquals(6.48, groceryService.calculateTotal(6.0, 0.48), 0.0001); }
    @Test void testCheckoutClearsGroceryItems() { groceryService.checkout(); assertEquals(0, groceryService.getItems().size()); }
    @Test void testClearGroceryRemovesAllItems() { groceryService.clearGrocery(); assertEquals(0, groceryService.getItems().size()); }
}
