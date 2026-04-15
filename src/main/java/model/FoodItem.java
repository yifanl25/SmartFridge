package model;

import java.time.LocalDate;

public class FoodItem {
    // Session-scoped unique identifier for inventory operations.
    private final String id;
    // Name selected from suggestion list (no custom free-form add in MVP).
    private final String name;
    // Ingredient category only; do not treat as recipe category.
    private final FoodCategory category;
    // PRD default quantity is 1 on Add Item success.
    private final int quantity;
    // Unit text for display/calculation context.
    private final String unit;
    // ISO date string for deterministic "newness" and sort-by-created calculations.
    private final String createdAt;
    // ISO date string used for urgency/warning/safe classification.
    private final String expiryDate;

    // Immutable inventory item for current demo session only (no persistence).
    public FoodItem(
            String id,
            String name,
            FoodCategory category,
            int quantity,
            String unit,
            String createdAt,
            String expiryDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
    }

    // Returns stable item id for view-level keying and item lookup.
    public String getId() {
        return id;
    }

    // Returns selected canonical food name.
    public String getName() {
        return name;
    }

    // Returns ingredient category for Inventory/Grocery filtering.
    public FoodCategory getCategory() {
        return category;
    }

    // Returns item amount; MVP add flow initializes this to 1.
    public int getQuantity() {
        return quantity;
    }

    // Returns quantity unit string.
    public String getUnit() {
        return unit;
    }

    // Returns creation timestamp used by "sort by createdAt".
    public String getCreatedAt() {
        return createdAt;
    }

    // Returns expiry timestamp used by urgency scoring and UI state.
    public String getExpiryDate() {
        return expiryDate;
    }

    // PRD "New": added within last 24 hours. Uses date granularity in current model.
    public boolean isNew() {
        LocalDate created = LocalDate.parse(createdAt);
        return !created.isBefore(LocalDate.now().minusDays(1));
    }

    // PRD "Urgent": expires today. Warning/safe breakdown handled at service/view layer.
    public boolean isUrgent() {
        LocalDate expiry = LocalDate.parse(expiryDate);
        return expiry.isEqual(LocalDate.now());
    }
}
