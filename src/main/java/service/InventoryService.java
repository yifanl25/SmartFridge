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
 */
public class InventoryService implements IInventoryService {
    /** Mutable backing list for this session.  */
    private final List<FoodItem> items;
    /** Catalog for resolve/suggest and expiry defaults.  */
    private final IFoodCatalog foodCatalog;

    /**
     * Creates an empty inventory bound to the given catalog.
     */
    public InventoryService(IFoodCatalog foodCatalog) {
        this.items = new ArrayList<>();
        this.foodCatalog = foodCatalog;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns a defensive copy to prevent external modification of the internal list.
     */
    @Override
    public List<FoodItem> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Resolves the food name from catalog suggestions or direct match.
     * Defaults to a fallback entry if no match is found.
     *
     * Generated fields:
     * - category from catalog entry
     * - quantity defaulted to 1
     * - creation date set to today
     * - expiry date = today + default shelf life
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

    /** {@inheritDoc}
     *
     * <p>
     * Adds a batch of food items into the inventory.
     */
    @Override
    public void addAllItems(List<FoodItem> toAdd) {
        items.addAll(toAdd);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Filters food items by category name (case-insensitive match).
     * Note: This is for inventory food categories, not recipe categories.
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
     * Sorts items by expiry date in ascending order (earliest expiry first).
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
     * Sorts items by creation time in descending order (newest first).
     */
    @Override
    public List<FoodItem> sortByCreatedTime() {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /** {@inheritDoc}
     *
     * <p>
     * Clears all items from the inventory.
     */
    @Override
    public void clearInventory() {
        items.clear();
    }
}
