package loader;

import model.FoodCatalogEntry;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JsonFoodCatalogLoader.
 * Covers classpath loading, filesystem loading, error handling,
 * and the safe wrapper method.
 */
public class TestJsonFoodCatalogLoader {

    /**
     * Tests loadFromFile() reads from classpath successfully.
     * Branch: getResourceAsStream finds the file → read from classpath.
     */
    @Test
    void testLoadFromFileReadsFromClasspath() throws IOException {
        List<FoodCatalogEntry> entries =
                JsonFoodCatalogLoader.loadFromFile("/food_catalog.json");
        assertFalse(entries.isEmpty());
        assertNotNull(entries.get(0).getFoodName());
        assertNotNull(entries.get(0).getCategory());
    }



    /**
     * Tests loadFromFileSafe() returns entries successfully for valid file.
     * Branch: no IOException → return list normally.
     */
    @Test
    void testLoadFromFileSafeReturnsEntriesForValidFile() {
        List<FoodCatalogEntry> entries =
                JsonFoodCatalogLoader.loadFromFileSafe("/food_catalog.json");
        assertFalse(entries.isEmpty());
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
                () -> JsonFoodCatalogLoader.loadFromFileSafe(tempFile.toString()));
        Files.deleteIfExists(tempFile);
    }


}