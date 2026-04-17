package model;

import java.time.LocalDate;

/**
 * Represents one inventory item in the current session.
 */
public class FoodItem {

    private final String id;
    private final String name;
    private final FoodCategory category;
    private final int quantity;
    private final String unit;
    private final String createdAt;
    private final String expiryDate;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Returns true if the item was added today or yesterday.
     */
    public boolean isNew() {
        LocalDate created = LocalDate.parse(createdAt);
        return !created.isBefore(LocalDate.now().minusDays(1));
    }

    /**
     * Returns true if the item expires today.
     */
    public boolean isUrgent() {
        LocalDate expiry = LocalDate.parse(expiryDate);
        return expiry.isEqual(LocalDate.now());
    }
}
