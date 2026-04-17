package service;

import model.FoodCatalogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Default in-memory implementation of {@link IFoodCatalog}.
 */
public class FoodCatalog implements IFoodCatalog {

    private final List<FoodCatalogEntry> entries;

    public FoodCatalog(List<FoodCatalogEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    /**
     * Normalizes text for comparison.
     */
    private static String norm(String text) {
        if (text == null) {
            return "";
        }
        return text.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if the given name matches the entry name or one of its aliases.
     */
    public boolean nameMatchesEntry(String name, FoodCatalogEntry entry) {
        String target = norm(name);

        if (target.isEmpty()) {
            return false;
        }

        if (norm(entry.getFoodName()).equals(target)) {
            return true;
        }

        for (String alias : entry.getAliases()) {
            if (norm(alias).equals(target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the given prefix matches the entry name or one of its aliases.
     */
    private boolean prefixMatchesEntry(String prefix, FoodCatalogEntry entry) {
        String target = norm(prefix);

        if (norm(entry.getFoodName()).startsWith(target)) {
            return true;
        }

        for (String alias : entry.getAliases()) {
            if (norm(alias).startsWith(target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns catalog suggestions for the given prefix.
     */
    @Override
    public List<FoodCatalogEntry> searchSuggestions(String prefix) {
        String target = prefix == null ? "" : prefix.trim().toLowerCase(Locale.ROOT);

        if (target.isEmpty()) {
            return new ArrayList<>();
        }

        List<FoodCatalogEntry> result = new ArrayList<>();

        for (FoodCatalogEntry entry : entries) {
            if (prefixMatchesEntry(prefix, entry)) {
                result.add(entry);
            }
        }

        return result;
    }

    /**
     * Returns true if the catalog contains the given food.
     */
    @Override
    public boolean containsFood(String foodName) {
        return resolveEntry(foodName).isPresent();
    }

    /**
     * Returns the default expiry days for one food.
     * Returns 3 if the food is not found.
     */
    @Override
    public int getDefaultExpiryDays(String foodName) {
        Optional<FoodCatalogEntry> entry = resolveEntry(foodName);

        if (entry.isPresent()) {
            return entry.get().getDefaultExpiryDays();
        }

        return 3;
    }

    /**
     * Resolves one food name to one catalog entry.
     */
    @Override
    public Optional<FoodCatalogEntry> resolveEntry(String foodName) {
        for (FoodCatalogEntry entry : entries) {
            if (nameMatchesEntry(foodName, entry)) {
                return Optional.of(entry);
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the canonical food name if found in the catalog.
     * Otherwise returns the trimmed raw input.
     */
    @Override
    public String canonicalFoodName(String raw) {
        Optional<FoodCatalogEntry> entry = resolveEntry(raw);

        if (entry.isPresent()) {
            return entry.get().getFoodName();
        }

        if (raw == null) {
            return "";
        }

        return raw.trim();
    }
}
