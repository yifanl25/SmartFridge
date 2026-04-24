package service;

import model.FoodCatalogEntry;
import model.FoodCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：Static food catalog lookup tests.(Maps to: {@link FoodCatalog} / {@link IFoodCatalog}).
 */
public class TestFoodCatalog {
    private FoodCatalog foodCatalog;

    /**
     * sets up a catalog which is aligned with {@code food_catalog.json}.
     */
    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat),
                new FoodCatalogEntry("Cheese", 14, cat)));
    }

    /**
     * Test: prefix-based search suggestions (used for add-item autocomplete).
     * Verification：number of matched results is as expected.
     * <p>
     * Maps to: {@link FoodCatalog#searchSuggestions(String)}
     */
    @Test
    void testSearchSuggestionsReturnsMatchingEntries() {
        assertEquals(1, foodCatalog.searchSuggestions("Mil").size());
    }

    /**
     * Test：Checks whether a food item exists in the catalog.
     * Verification：Known item returns true.
     * <p>
     * Maps to: {@link FoodCatalog#containsFood(String)}
     */
    @Test
    void testContainsFoodReturnsTrueWhenFoodExists() {
        assertTrue(foodCatalog.containsFood("Milk"));
    }

    /**
     * Test：Retrieves default expiry days for a given food item(used for computing default expiration dates).
     * Verification：Value matches catalog configuration.
     * <p>
     * Maps to: {@link FoodCatalog#getDefaultExpiryDays(String)}
     */
    @Test
    void testGetDefaultExpiryDaysReturnsConfiguredValue() {
        assertEquals(7, foodCatalog.getDefaultExpiryDays("Milk"));
    }
}
