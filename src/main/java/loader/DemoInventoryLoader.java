package loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;
import service.IFoodCatalog;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Optional demo seed loader: reads {@code data.json} (array of name/expiryDate/category) and maps to {@link FoodItem}.
 * Expiry comes from JSON; category/name are resolved via {@link IFoodCatalog} when possible.
 */
public final class DemoInventoryLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DemoInventoryLoader() {
    }

    /**
     * Loads from classpath (path starting with {@code /}) or filesystem; returns empty list if missing.
     *
     * @throws IOException if the file exists but is not valid JSON
     */
    public static List<FoodItem> loadFoodItemsOptional(String pathOrResource, IFoodCatalog foodCatalog) throws IOException {
        InputStream in = DemoInventoryLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource);
        if (in == null) {
            Path p = Path.of(pathOrResource);
            if (Files.isRegularFile(p)) {
                in = Files.newInputStream(p);
            } else {
                return new ArrayList<>();
            }
        }
        try (InputStream stream = in) {
            List<DemoInventoryRow> rows = MAPPER.readValue(stream, new TypeReference<List<DemoInventoryRow>>() { });
            List<FoodItem> out = new ArrayList<>();
            for (DemoInventoryRow row : rows) {
                out.add(toFoodItem(row, foodCatalog));
            }
            return out;
        }
    }

    /**
     * Same as {@link #loadFoodItemsOptional(String, IFoodCatalog)} but wraps {@link IOException} in {@link IllegalStateException}.
     */
    public static List<FoodItem> loadFoodItemsSafe(String pathOrResource, IFoodCatalog foodCatalog) {
        try {
            return loadFoodItemsOptional(pathOrResource, foodCatalog);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load demo inventory: " + pathOrResource, e);
        }
    }

    /**
     * Maps one JSON row to a {@link FoodItem} using catalog resolution or JSON category fallback.
     */
    private static FoodItem toFoodItem(DemoInventoryRow row, IFoodCatalog foodCatalog) {
        String name = row.name == null ? "" : row.name.trim();
        Optional<FoodCatalogEntry> resolved = foodCatalog.resolveEntry(name);
        FoodCategory category;
        String displayName;
        if (resolved.isPresent()) {
            FoodCatalogEntry entry = resolved.get();
            category = entry.getCategory();
            displayName = foodCatalog.canonicalFoodName(name);
            if (displayName.isEmpty()) {
                displayName = entry.getFoodName();
            }
        } else {
            category = fallbackCategory(row.category);
            displayName = name.isEmpty() ? "Unknown" : name;
        }
        String createdAt = LocalDate.now().toString();
        String expiry = row.expiryDate == null ? createdAt : row.expiryDate.trim();
        return new FoodItem(
                UUID.randomUUID().toString(),
                displayName,
                category,
                1,
                "pcs",
                createdAt,
                expiry);
    }

    /**
     * Builds a synthetic {@link FoodCategory} when catalog has no entry for the demo name.
     */
    private static FoodCategory fallbackCategory(String label) {
        String name = label == null || label.trim().isEmpty() ? "Misc" : label.trim();
        String id = "demo-" + name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        return new FoodCategory(id, name, "box");
    }

    /** Jackson DTO for one array element in {@code data.json}.  */
    @SuppressWarnings("unused")
    private static class DemoInventoryRow {
        public String name;
        public String expiryDate;
        public String category;
    }
}
