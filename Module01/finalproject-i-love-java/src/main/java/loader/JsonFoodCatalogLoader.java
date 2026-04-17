package loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.FoodCatalogEntry;
import model.FoodCategory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads food catalog data from JSON.
 */
public final class JsonFoodCatalogLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonFoodCatalogLoader() {
    }

    /**
     * Loads the catalog from a classpath resource or a file path.
     */
    public static List<FoodCatalogEntry> loadFromFile(String pathOrResource) throws IOException {
        InputStream in = JsonFoodCatalogLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource
        );

        if (in == null) {
            in = Files.newInputStream(Path.of(pathOrResource));
        }

        try (InputStream stream = in) {
            FoodCatalogFile file = MAPPER.readValue(stream, FoodCatalogFile.class);
            List<FoodCatalogEntry> result = new ArrayList<>();

            for (FoodCatalogEntryDto row : file.entries) {
                FoodCategory category = new FoodCategory(
                        row.category.id,
                        row.category.name,
                        row.category.icon
                );

                List<String> aliases;
                if (row.aliases == null) {
                    aliases = new ArrayList<>();
                } else {
                    aliases = row.aliases;
                }

                result.add(new FoodCatalogEntry(
                        row.id,
                        row.foodName,
                        aliases,
                        row.defaultExpiryDays,
                        category
                ));
            }

            return result;
        }
    }

    /**
     * Loads the catalog and wraps checked exceptions as IllegalStateException.
     */
    public static List<FoodCatalogEntry> loadFromFileSafe(String pathOrResource) {
        try {
            return loadFromFile(pathOrResource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load food catalog: " + pathOrResource, e);
        }
    }

    @SuppressWarnings("unused")
    private static class FoodCatalogFile {
        public List<FoodCatalogEntryDto> entries;
    }

    @SuppressWarnings("unused")
    private static class FoodCatalogEntryDto {
        public String id;
        public String foodName;
        public List<String> aliases;
        public int defaultExpiryDays;
        public CategoryDto category;
    }

    @SuppressWarnings("unused")
    private static class CategoryDto {
        public String id;
        public String name;
        public String icon;
    }
}
