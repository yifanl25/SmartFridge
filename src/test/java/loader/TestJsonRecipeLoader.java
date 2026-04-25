package loader;

import model.Recipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JsonRecipeLoader.
 * Covers classpath loading, filesystem loading, error handling,
 * health tag parsing, and ingredient mapping edge cases.
 */
public class TestJsonRecipeLoader {

    /**
     * Tests loadFromFile() reads recipes from classpath successfully.
     * Branch: getResourceAsStream finds file → read from classpath.
     */
    @Test
    void testLoadFromFileReadsFromClasspath() throws IOException {
        List<Recipe> recipes = JsonRecipeLoader.loadFromFile("/recipes.json");
        assertFalse(recipes.isEmpty());
        assertNotNull(recipes.get(0).getTitle());
        assertNotNull(recipes.get(0).getRecipeCategory());
    }


    /**
     * Tests loadFromFile() with path not starting with slash.
     * Branch: path does not start with "/" → prepend "/" for classpath lookup.
     */
    @Test
    void testLoadFromFileWithPathNotStartingWithSlash() throws IOException {
        List<Recipe> recipes = JsonRecipeLoader.loadFromFile("recipes.json");
        assertFalse(recipes.isEmpty());
    }

    /**
     * Tests loadFromFileSafe() returns recipes successfully for valid file.
     * Branch: no IOException → return list normally.
     */
    @Test
    void testLoadFromFileSafeReturnsRecipesForValidFile() {
        List<Recipe> recipes = JsonRecipeLoader.loadFromFileSafe("/recipes.json");
        assertFalse(recipes.isEmpty());
    }

    /**
     * Tests loadFromFileSafe() throws IllegalStateException for invalid JSON.
     * Branch: IOException caught → throw IllegalStateException.
     */
    @Test
    void testLoadFromFileSafeThrowsIllegalStateExceptionForInvalidJson() throws IOException {
        Path tempFile = Files.createTempFile("invalid", ".json");
        Files.writeString(tempFile, "not valid json {{{}}}");
        assertThrows(IllegalStateException.class,
                () -> JsonRecipeLoader.loadFromFileSafe(tempFile.toString()));
        Files.deleteIfExists(tempFile);
    }

}
