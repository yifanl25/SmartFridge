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

public class InventoryService implements IInventoryService {
    // Session-only inventory state. Must be reset after checkout.
    private final List<FoodItem> items;
    // Static catalog dependency for category/default expiry derivation.
    private final IFoodCatalog foodCatalog;

    // Construct in-memory inventory service for current demo run.
    public InventoryService(IFoodCatalog foodCatalog) {
        this.items = new ArrayList<>();
        this.foodCatalog = foodCatalog;
    }

    @Override
    // Return defensive copy so callers cannot mutate service state directly.
    public List<FoodItem> getAllItems() {
        return new ArrayList<>(items);
    }

    @Override
    // Add item from selected suggestion name.
    // PRD implementation details:
    // - name comes from suggestion selection
    // - category comes from food_catalog mapping
    // - quantity defaults to 1
    // - createdAt = current date/time base (date granularity in current model)
    // - expiryDate = createdAt + defaultExpiryDays
    public FoodItem addItem(String foodName) {
        FoodCategory category = foodCatalog.searchSuggestions(foodName).stream()
                .findFirst()
                .map(FoodCatalogEntry::getCategory)
                .orElse(new FoodCategory("misc", "Misc", "box"));
        String createdAt = LocalDate.now().toString();
        String expiryDate = LocalDate.now().plusDays(foodCatalog.getDefaultExpiryDays(foodName)).toString();
        FoodItem item = new FoodItem(
                UUID.randomUUID().toString(),
                foodName,
                category,
                1,
                "pcs",
                createdAt,
                expiryDate);
        items.add(item);
        return item;
    }

    @Override
    // Filter by ingredient category for Inventory page.
    // Do not reuse this method for recommendation category filtering.
    public List<FoodItem> filterByCategory(String categoryName) {
        return items.stream()
                .filter(i -> i.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }

    @Override
    // Sort by expiry timeline; nearest expiry first for urgency-oriented display.
    public List<FoodItem> sortByExpiry() {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    @Override
    // Sort by creation time descending so newest inventory appears first.
    public List<FoodItem> sortByCreatedTime() {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    // Session cleanup hook for loop end.
    public void clearInventory() {
        items.clear();
    }
}
