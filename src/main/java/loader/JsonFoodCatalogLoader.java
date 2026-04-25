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
 * Loads {@code food_catalog.json} into {@link FoodCatalogEntry} list (classpath or absolute file path).
 */
public final class JsonFoodCatalogLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonFoodCatalogLoader() {
    }

    /**
     * Reads JSON: tries {@code getResourceAsStream} first, then {@link Files#newInputStream(Path)}.
     *
     * @param pathOrResource e.g. {@code /food_catalog.json} or disk path
     */
    public static List<FoodCatalogEntry> loadFromFile(String pathOrResource) throws IOException {
        InputStream in = JsonFoodCatalogLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource);
        if (in == null) {
            in = Files.newInputStream(Path.of(pathOrResource));
        }
        try (InputStream stream = in) {
            FoodCatalogFile file = MAPPER.readValue(stream, FoodCatalogFile.class);
            List<FoodCatalogEntry> out = new ArrayList<>();
            for (FoodCatalogEntryDto row : file.entries) {
                FoodCategory cat = new FoodCategory(
                        row.category.id,
                        row.category.name,
                        row.category.icon);
                List<String> aliases = row.aliases == null ? List.of() : row.aliases;
                out.add(new FoodCatalogEntry(
                        row.id,
                        row.foodName,
                        aliases,
                        row.defaultExpiryDays,
                        cat));
            }
            return out;
        }
    }

    /**
     * Same as {@link #loadFromFile(String)} for app entrypoints; wraps failure in {@link IllegalStateException}.
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
