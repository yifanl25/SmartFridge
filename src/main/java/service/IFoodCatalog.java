package service;

import model.FoodCatalogEntry;

import java.util.List;

public interface IFoodCatalog {
    // Add Item modal contract:
    // return suggestion candidates by typed prefix, capped to 3 by caller/service policy.
    // Suggestions must come from static food_catalog.json only.
    List<FoodCatalogEntry> searchSuggestions(String prefix);

    // Validation contract:
    // true only when the given name is a canonical catalog item (or alias-resolved by implementation).
    boolean containsFood(String foodName);

    // Expiry contract:
    // return defaultExpiryDays configured in food_catalog.json for a canonical name.
    // Used by InventoryService when constructing FoodItem.expiryDate.
    int getDefaultExpiryDays(String foodName);
}
