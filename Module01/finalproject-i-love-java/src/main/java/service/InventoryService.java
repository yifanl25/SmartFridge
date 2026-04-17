package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Stores the inventory in memory for one demo session.
 */
public class InventoryService implements IInventoryService {

    private final List<FoodItem> items;
    private final IFoodCatalog foodCatalog;

    public InventoryService(IFoodCatalog foodCatalog) {
        this.items = new ArrayList<>();
        this.foodCatalog = foodCatalog;
    }

    /**
     * Returns all inventory items.
     */
    @Override
    public List<FoodItem> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Adds one item to the inventory.
     */
    @Override
    public FoodItem addItem(String foodName) {
        FoodCatalogEntry entry = null;

        if (foodCatalog.resolveEntry(foodName).isPresent()) {
            entry = foodCatalog.resolveEntry(foodName).get();
        } else {
            List<FoodCatalogEntry> suggestions = foodCatalog.searchSuggestions(foodName);
            if (!suggestions.isEmpty()) {
                entry = suggestions.get(0);
            }
        }

        if (entry == null) {
            entry = new FoodCatalogEntry(
                    foodName,
                    3,
                    new FoodCategory("misc", "Misc", "box")
            );
        }

        FoodCategory category = entry.getCategory();
        String canonicalName = foodCatalog.canonicalFoodName(foodName);
        String createdAt = LocalDate.now().toString();
        String expiryDate = LocalDate.now()
                .plusDays(foodCatalog.getDefaultExpiryDays(canonicalName))
                .toString();

        FoodItem item = new FoodItem(
                UUID.randomUUID().toString(),
                canonicalName,
                category,
                1,
                "pcs",
                createdAt,
                expiryDate
        );

        items.add(item);
        return item;
    }

    /**
     * Adds a list of items to the inventory.
     */
    @Override
    public void addAllItems(List<FoodItem> toAdd) {
        items.addAll(toAdd);
    }

    /**
     * Filters inventory items by category name.
     */
    @Override
    public List<FoodItem> filterByCategory(String categoryName) {
        List<FoodItem> result = new ArrayList<>();

        for (FoodItem item : items) {
            if (item.getCategory().getName().equalsIgnoreCase(categoryName)) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Sorts inventory items by expiry date.
     */
    @Override
    public List<FoodItem> sortByExpiry() {
        List<FoodItem> result = new ArrayList<>(items);
        result.sort(Comparator.comparing(FoodItem::getExpiryDate));
        return result;
    }

    /**
     * Sorts inventory items by created time.
     */
    @Override
    public List<FoodItem> sortByCreatedTime() {
        List<FoodItem> result = new ArrayList<>(items);
        result.sort(Comparator.comparing(FoodItem::getCreatedAt).reversed());
        return result;
    }

    /**
     * Clears the inventory.
     */
    @Override
    public void clearInventory() {
        items.clear();
    }
}
