package loader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RecipeJsonFile.
 * Verifies constructor and getPath() work correctly.
 */
public class TestRecipeJsonFileLoader {

    /**
     * Tests that getPath() returns the path passed to the constructor.
     */
    @Test
    void testGetPathReturnsConstructorValue() {
        RecipeJsonFile file = new RecipeJsonFile("/recipes.json");
        assertEquals("/recipes.json", file.getPath());
    }

    /**
     * Tests that getPath() works with a filesystem path.
     */
    @Test
    void testGetPathWithFilesystemPath() {
        RecipeJsonFile file = new RecipeJsonFile("data/recipes.json");
        assertEquals("data/recipes.json", file.getPath());
    }

    /**
     * Tests that getPath() returns empty string when empty string is passed.
     */
    @Test
    void testGetPathWithEmptyString() {
        RecipeJsonFile file = new RecipeJsonFile("");
        assertEquals("", file.getPath());
    }
}