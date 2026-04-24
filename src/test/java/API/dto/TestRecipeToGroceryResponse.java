package API.dto;

import api.dto.RecipeToGroceryResponse;
import model.FoodCategory;
import model.GroceryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RecipeToGroceryResponse DTO.
 * Verifies all getters and setters work correctly.
 */
public class TestRecipeToGroceryResponse {

    private RecipeToGroceryResponse response;

    @BeforeEach
    void setUp() {
        response = new RecipeToGroceryResponse();
    }

    @Test
    void testSetAndGetRecipeId() {
        response.setRecipeId("r1");
        assertEquals("r1", response.getRecipeId());
    }

    @Test
    void testSetAndGetRecipeTitle() {
        response.setRecipeTitle("Egg Bowl");
        assertEquals("Egg Bowl", response.getRecipeTitle());
    }

    @Test
    void testSetAndGetAddedCount() {
        response.setAddedCount(3);
        assertEquals(3, response.getAddedCount());
    }

    @Test
    void testSetAndGetMergedCount() {
        response.setMergedCount(2);
        assertEquals(2, response.getMergedCount());
    }

    @Test
    void testSetAndGetAddedItems() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        GroceryItem item = new GroceryItem("g1", "Milk", cat, 2, 3.0, false);
        response.setAddedItems(List.of(item));
        assertEquals(1, response.getAddedItems().size());
        assertEquals("Milk", response.getAddedItems().get(0).getName());
    }


}