package loader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FoodCatalogJsonFile.
 * Verifies constructor and getPath() work correctly.
 */
public class TestFoodCatalogJsonFileLoader {

    /**
     * Tests that getPath() returns the path passed to the constructor.
     */
    @Test
    void testGetPathReturnsConstructorValue() {
        FoodCatalogJsonFile file = new FoodCatalogJsonFile("/food_catalog.json");
        assertEquals("/food_catalog.json", file.getPath());
    }


}