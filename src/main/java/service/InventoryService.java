package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * In-memory inventory for one demo session; uses {@link IFoodCatalog} for category and default expiry.
 * <p>
 * 单次演示会话的内存库存；依赖 {@link IFoodCatalog} 解析分类与默认过期日。
 */
public class InventoryService implements IInventoryService {
    /** Mutable backing list for this session. / 本会话的可变 backing 列表。 */
    private final List<FoodItem> items;
    /** Catalog for resolve/suggest and expiry defaults. / 用于解析/联想与默认保质期的目录。 */
    private final IFoodCatalog foodCatalog;

    /**
     * Creates an empty inventory bound to the given catalog.
     * <p>
     * 创建绑定到给定目录的空库存。
     */
    public InventoryService(IFoodCatalog foodCatalog) {
        this.items = new ArrayList<>();
        this.foodCatalog = foodCatalog;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 返回防御性拷贝，避免调用方直接改内部列表。
     */
    @Override
    public List<FoodItem> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 名称来自建议选择；分类来自目录；数量默认 1；创建日为今天；过期日 = 今天 + 默认保质天数。
     */
    @Override
    public FoodItem addItem(String foodName) {
        FoodCatalogEntry entry = foodCatalog.resolveEntry(foodName)
                .orElseGet(() -> foodCatalog.searchSuggestions(foodName).stream().findFirst().orElse(null));
        if (entry == null) {
            entry = new FoodCatalogEntry(foodName, 3, new FoodCategory("misc", "Misc", "box"));
        }
        FoodCategory category = entry.getCategory();
        String canonicalName = foodCatalog.canonicalFoodName(foodName);
        String createdAt = LocalDate.now().toString();
        String expiryDate = LocalDate.now().plusDays(foodCatalog.getDefaultExpiryDays(canonicalName)).toString();
        FoodItem item = new FoodItem(
                UUID.randomUUID().toString(),
                canonicalName,
                category,
                1,
                "pcs",
                createdAt,
                expiryDate);
        items.add(item);
        return item;
    }

    /** {@inheritDoc} */
    @Override
    public void addAllItems(List<FoodItem> toAdd) {
        items.addAll(toAdd);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 按食材分类显示名等值匹配（忽略大小写）。勿用于菜谱分类筛选。
     */
    @Override
    public List<FoodItem> filterByCategory(String categoryName) {
        return items.stream()
                .filter(i -> i.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * 按过期日升序（越早越靠前）。
     */
    @Override
    public List<FoodItem> sortByExpiry() {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * 按创建日降序（新到旧）。
     */
    @Override
    public List<FoodItem> sortByCreatedTime() {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public void clearInventory() {
        items.clear();
    }
}
