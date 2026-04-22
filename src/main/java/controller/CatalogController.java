package controller;

import model.FoodCatalogEntry;
import service.IFoodCatalog;

import java.util.List;
import java.util.Optional;

/**
 * Internal coordination layer for catalog lookups and canonical name resolution.
 * <p>
 * This is not a Spring MVC controller. HTTP routing stays in {@code api.web.CatalogApiController};
 * this class exists so API code can delegate through the same controller layer used elsewhere.
 */
public class CatalogController {
    private final IFoodCatalog foodCatalog;

    public CatalogController(IFoodCatalog foodCatalog) {
        this.foodCatalog = foodCatalog;
    }

    public List<FoodCatalogEntry> searchSuggestions(String prefix) {
        return foodCatalog.searchSuggestions(prefix);
    }

    public Optional<FoodCatalogEntry> resolveEntry(String foodName) {
        return foodCatalog.resolveEntry(foodName);
    }

    public String canonicalFoodName(String raw) {
        return foodCatalog.canonicalFoodName(raw);
    }

    public int getDefaultExpiryDays(String foodName) {
        return foodCatalog.getDefaultExpiryDays(foodName);
    }
}
