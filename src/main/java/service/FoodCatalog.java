package service;

import model.FoodCatalogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FoodCatalog implements IFoodCatalog {
    // In-memory snapshot of static catalog rows loaded at app startup.
    private final List<FoodCatalogEntry> entries;

    // Build immutable catalog facade over static JSON rows (session-independent read model).
    public FoodCatalog(List<FoodCatalogEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    @Override
    // Prefix search used by AddItem modal suggestions.
    // PRD behavior notes:
    // - normalize to lowercase + trim spaces before matching
    // - caller should limit displayed count to top 3 suggestions
    public List<FoodCatalogEntry> searchSuggestions(String prefix) {
        String normalized = prefix == null ? "" : prefix.trim().toLowerCase(Locale.ROOT);
        return entries.stream()
                .filter(e -> e.getFoodName().toLowerCase(Locale.ROOT).startsWith(normalized))
                .collect(Collectors.toList());
    }

    @Override
    // Returns whether a food exists in canonical catalog.
    // Alias resolution can be layered here later without changing interface.
    public boolean containsFood(String foodName) {
        return entries.stream().anyMatch(e -> e.getFoodName().equalsIgnoreCase(foodName));
    }

    @Override
    // Lookup default expiry window for Add Item generated FoodItem.
    // Fallback is a defensive default when catalog row is missing.
    public int getDefaultExpiryDays(String foodName) {
        return entries.stream()
                .filter(e -> e.getFoodName().equalsIgnoreCase(foodName))
                .findFirst()
                .map(FoodCatalogEntry::getDefaultExpiryDays)
                .orElse(3);
    }
}
