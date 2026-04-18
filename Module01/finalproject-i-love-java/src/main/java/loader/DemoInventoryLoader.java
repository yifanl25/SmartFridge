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
 * Loads optional demo inventory data from data.json.
 */
public final class DemoInventoryLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DemoInventoryLoader() {
    }

    /**
     * Loads demo inventory from a classpath resource or a file path.
     * Returns an empty list if the resource does not exist.
     */
    public static List<FoodItem> loadFoodItemsOptional(
            String pathOrResource,
            IFoodCatalog foodCatalog) throws IOException {

        InputStream in = DemoInventoryLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource
        );

        if (in == null) {
            Path path = Path.of(pathOrResource);

            if (Files.isRegularFile(path)) {
                in = Files.newInputStream(path);
            } else {
                return new ArrayList<>();
            }
        }

        try (InputStream stream = in) {
            List<DemoInventoryRow> rows = MAPPER.readValue(
                    stream,
                    new TypeReference<List<DemoInventoryRow>>() { }
            );

            List<FoodItem> result = new ArrayList<>();

            for (DemoInventoryRow row : rows) {
                result.add(toFoodItem(row, foodCatalog));
            }

            return result;
        }
    }

    /**
     * Loads demo inventory and wraps checked exceptions as IllegalStateException.
     */
    public static List<FoodItem> loadFoodItemsSafe(
            String pathOrResource,
            IFoodCatalog foodCatalog) {
        try {
            return loadFoodItemsOptional(pathOrResource, foodCatalog);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load demo inventory: " + pathOrResource, e);
        }
    }

    /**
     * Converts one JSON row to one FoodItem.
     */
    private static FoodItem toFoodItem(DemoInventoryRow row, IFoodCatalog foodCatalog) {
        String rawName;
        if (row.name == null) {
            rawName = "";
        } else {
            rawName = row.name.trim();
        }

        Optional<FoodCatalogEntry> resolved = foodCatalog.resolveEntry(rawName);

        FoodCategory category;
        String displayName;

        if (resolved.isPresent()) {
            FoodCatalogEntry entry = resolved.get();
            category = entry.getCategory();
            displayName = foodCatalog.canonicalFoodName(rawName);

            if (displayName.isEmpty()) {
                displayName = entry.getFoodName();
            }
        } else {
            category = fallbackCategory(row.category);

            if (rawName.isEmpty()) {
                displayName = "Unknown";
            } else {
                displayName = rawName;
            }
        }

        String createdAt = LocalDate.now().toString();
        String expiryDate;

        if (row.expiryDate == null) {
            expiryDate = createdAt;
        } else {
            expiryDate = row.expiryDate.trim();
        }

        return new FoodItem(
                UUID.randomUUID().toString(),
                displayName,
                category,
                1,
                "pcs",
                createdAt,
                expiryDate
        );
    }

    /**
     * Builds a fallback category when the food is not found in the catalog.
     */
    private static FoodCategory fallbackCategory(String label) {
        String name;

        if (label == null || label.trim().isEmpty()) {
            name = "Misc";
        } else {
            name = label.trim();
        }

        String id = "demo-" + name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        return new FoodCategory(id, name, "box");
    }

    @SuppressWarnings("unused")
    private static class DemoInventoryRow {
        public String name;
        public String expiryDate;
        public String category;
    }
}
