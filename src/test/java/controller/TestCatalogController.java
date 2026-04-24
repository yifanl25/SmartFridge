package controller;

import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.IFoodCatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD: CatalogController delegation tests.
 */
public class TestCatalogController {

    private CatalogController catalogController;
    private FoodCategory cat;

    @BeforeEach
    void setUp() {
        cat = new FoodCategory("c1", "Dairy", "milk");
        IFoodCatalog foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat),
                new FoodCatalogEntry("Cheese", 14, cat)));
        catalogController = new CatalogController(foodCatalog);
    }

    /**
     * Tests searchSuggestions() returns matching entries for a given prefix.
     */
    @Test
    void testSearchSuggestionsReturnMatchingEntries() {
        List<FoodCatalogEntry> result = catalogController.searchSuggestions("Mil");
        assertEquals(1, result.size());
        assertEquals("Milk", result.get(0).getFoodName());
    }



    /**
     * Tests searchSuggestions() returns empty list for non-matching prefix.
     */
    @Test
    void testSearchSuggestionsReturnsEmptyForNonMatchingPrefix() {
        List<FoodCatalogEntry> result = catalogController.searchSuggestions("xyz");
        assertEquals(0, result.size());
    }

    /**
     * Tests resolveEntry() returns present Optional for known food name.
     */
    @Test
    void testResolveEntryReturnsPresentForKnownFood() {
        Optional<FoodCatalogEntry> result = catalogController.resolveEntry("Milk");
        assertTrue(result.isPresent());
        assertEquals("Milk", result.get().getFoodName());
    }

    /**
     * Tests resolveEntry() returns empty Optional for unknown food name.
     */
    @Test
    void testResolveEntryReturnsEmptyForUnknownFood() {
        Optional<FoodCatalogEntry> result = catalogController.resolveEntry("Pizza");
        assertTrue(result.isEmpty());
    }

    /**
     * Tests canonicalFoodName() returns the canonical name for a known food.
     */
    @Test
    void testCanonicalFoodNameReturnsCorrectName() {
        String result = catalogController.canonicalFoodName("Milk");
        assertEquals("Milk", result);
    }

    /**
     * Tests getDefaultExpiryDays() returns configured shelf life for known food.
     */
    @Test
    void testGetDefaultExpiryDaysReturnsConfiguredValue() {
        assertEquals(7, catalogController.getDefaultExpiryDays("Milk"));
    }

    /**
     * Tests getDefaultExpiryDays() for a different item returns its own value.
     */
    @Test
    void testGetDefaultExpiryDaysReturnsCorrectValueForCheese() {
        assertEquals(14, catalogController.getDefaultExpiryDays("Cheese"));
    }
}