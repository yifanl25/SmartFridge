package org.example.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodItem {
    private String id;
    private String name;
    private LocalDate expiryDate;
    private String category;
    private String storageArea;

    public FoodItem() {
    }

    public FoodItem(String id, String name, LocalDate expiryDate, String category) {
        this(id, name, expiryDate, category, "Fridge");
    }

    public FoodItem(String id, String name, LocalDate expiryDate, String category, String storageArea) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.category = category;
        this.storageArea = storageArea;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public String getStorageArea() {
        return (storageArea == null || storageArea.isBlank()) ? "Fridge" : storageArea;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStorageArea(String storageArea) {
        this.storageArea = storageArea;
    }

    public long getDaysLeft() {
        if (expiryDate == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
}