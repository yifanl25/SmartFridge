package service;

import model.FoodCatalogEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default {@link IFoodCatalog} backed by a static list loaded from JSON at startup.
 */
public class FoodCatalog implements IFoodCatalog {
    /** In-memory catalog rows.  */
    private final List<FoodCatalogEntry> entries;

    /**
     * @param entries catalog rows (copied defensively into this instance)
     */
    public FoodCatalog(List<FoodCatalogEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    /**
     * Normalizes strings for case-insensitive compare (trim + lower ROOT locale).
     */
    private static String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Whether {@code name} equals this entry's food name or any alias (used by scoring and resolve).
     */
    public boolean nameMatchesEntry(String name, FoodCatalogEntry e) {
        String n = norm(name);
        if (n.isEmpty()) {
            return false;
        }
        if (norm(e.getFoodName()).equals(n)) {
            return true;
        }
        for (String a : e.getAliases()) {
            if (norm(a).equals(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether entry matches non-empty prefix on food name or aliases.
     */
    private boolean prefixMatchesEntry(String rawPrefix, FoodCatalogEntry e) {
        String p = norm(rawPrefix);
        if (norm(e.getFoodName()).startsWith(p)) {
            return true;
        }
        for (String a : e.getAliases()) {
            if (norm(a).startsWith(p)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<FoodCatalogEntry> searchSuggestions(String prefix) {
        String p = prefix == null ? "" : prefix.trim().toLowerCase(Locale.ROOT);
        if (p.isEmpty()) {
            return List.of();
        }
        return entries.stream()
                .filter(e -> prefixMatchesEntry(prefix, e))
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsFood(String foodName) {
        return resolveEntry(foodName).isPresent();
    }

    /** {@inheritDoc} */
    @Override
    public int getDefaultExpiryDays(String foodName) {
        return resolveEntry(foodName)
                .map(FoodCatalogEntry::getDefaultExpiryDays)
                .orElse(3);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<FoodCatalogEntry> resolveEntry(String foodName) {
        return entries.stream()
                .filter(e -> nameMatchesEntry(foodName, e))
                .findFirst();
    }

    /** {@inheritDoc} */
    @Override
    public String canonicalFoodName(String raw) {
        return resolveEntry(raw)
                .map(FoodCatalogEntry::getFoodName)
                .orElse(raw == null ? "" : raw.trim());
    }
}
