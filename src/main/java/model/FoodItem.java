package model;

import java.time.LocalDate;

/**
 * One inventory row in the current session (no persistence across runs).
 */
public class FoodItem {
    /** Unique id for list keys and updates.  */
    private final String id;
    /** Canonical food name (from catalog suggestions).  */
    private final String name;
    /** Ingredient category for inventory filters.  */
    private final FoodCategory category;
    /** Amount on hand.  */
    private final int quantity;
    /** Unit label (e.g. pcs).  */
    private final String unit;
    /** ISO date string when the item was added (date granularity).  */
    private final String createdAt;
    /** ISO date string for expiry.  */
    private final String expiryDate;

    /**
     * Builds an immutable inventory snapshot.
     */
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

    /** Returns item id.  */
    public String getId() {
        return id;
    }

    /** Returns canonical food name.  */
    public String getName() {
        return name;
    }

    /** Returns ingredient category. */
    public FoodCategory getCategory() {
        return category;
    }

    /** Returns quantity.  */
    public int getQuantity() {
        return quantity;
    }

    /** Returns unit string.  */
    public String getUnit() {
        return unit;
    }

    /** Returns created-at date string.  */
    public String getCreatedAt() {
        return createdAt;
    }

    /** Returns expiry date string.  */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * PRD "new" item: created on or after yesterday (date-level model).
     */
    public boolean isNew() {
        LocalDate created = LocalDate.parse(createdAt);
        return !created.isBefore(LocalDate.now().minusDays(1));
    }

    /**
     * PRD "urgent" item: expires today.
     */
    public boolean isUrgent() {
        LocalDate expiry = LocalDate.parse(expiryDate);
        return expiry.isEqual(LocalDate.now());
    }
}
