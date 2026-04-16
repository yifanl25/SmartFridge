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
 * <p>
 * 可选演示种子加载器：读取 {@code data.json}（name/expiryDate/category 数组）并映射为 {@link FoodItem}。
 * 过期日来自 JSON；名称与分类尽可能通过 {@link IFoodCatalog} 解析。
 */
public final class DemoInventoryLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DemoInventoryLoader() {
    }

    /**
     * Loads from classpath (path starting with {@code /}) or filesystem; returns empty list if missing.
     * <p>
     * 从 classpath（以 {@code /} 开头）或文件系统加载；资源不存在则返回空列表。
     *
     * @throws IOException if the file exists but is not valid JSON / 文件存在但 JSON 无效时抛出
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
     * <p>
     * 与 {@link #loadFoodItemsOptional(String, IFoodCatalog)} 相同，但将 {@link IOException} 包装为 {@link IllegalStateException}。
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
     * <p>
     * 将一行 JSON 映射为 {@link FoodItem}：优先目录解析，否则用 JSON 中的 category 兜底。
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
     * <p>
     * 当目录无对应条目时，用演示数据中的分类字符串构造合成 {@link FoodCategory}。
     */
    private static FoodCategory fallbackCategory(String label) {
        String name = label == null || label.trim().isEmpty() ? "Misc" : label.trim();
        String id = "demo-" + name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        return new FoodCategory(id, name, "box");
    }

    /** Jackson DTO for one array element in {@code data.json}. / {@code data.json} 数组元素的 Jackson DTO。 */
    @SuppressWarnings("unused")
    private static class DemoInventoryRow {
        public String name;
        public String expiryDate;
        public String category;
    }
}
