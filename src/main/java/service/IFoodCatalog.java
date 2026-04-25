package service;

import model.FoodCatalogEntry;

import java.util.List;
import java.util.Optional;

/**
 * Read-only food catalog: suggestions, membership, default expiry, alias resolution.
 */
public interface IFoodCatalog {

    /**
     * Prefix search for Add Item suggestion list.
     */
    List<FoodCatalogEntry> searchSuggestions(String prefix);

    /**
     * Whether the catalog recognizes this food name (including aliases).
     */
    boolean containsFood(String foodName);

    /**
     * Default shelf-life offset in days for inventory expiry when adding this food.
     */
    int getDefaultExpiryDays(String foodName);

    /**
     * Resolves a catalog row by canonical name or alias (trim/lowercase normalization in implementation).
     */
    Optional<FoodCatalogEntry> resolveEntry(String foodName);

    /**
     * Canonical display string for cross-matching recipe ingredients vs inventory names.
     */
    String canonicalFoodName(String raw);
}
