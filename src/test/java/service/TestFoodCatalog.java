package service;

import model.FoodCatalogEntry;
import model.FoodCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFoodCatalog {
    private FoodCatalog foodCatalog;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat),
                new FoodCatalogEntry("Cheese", 14, cat)));
    }

    @Test
    void testSearchSuggestionsReturnsMatchingEntries() {
        assertEquals(1, foodCatalog.searchSuggestions("Mil").size());
    }

    @Test
    void testContainsFoodReturnsTrueWhenFoodExists() {
        assertTrue(foodCatalog.containsFood("Milk"));
    }

    @Test
    void testGetDefaultExpiryDaysReturnsConfiguredValue() {
        assertEquals(7, foodCatalog.getDefaultExpiryDays("Milk"));
    }
}
