package org.example.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryManager {
    private static final String FILE_PATH = "data.json";

    private final List<FoodItem> inventory = new ArrayList<>();
    private final ObjectMapper mapper;

    public InventoryManager() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                mapper.writeValue(file, inventory);
                return;
            }

            List<FoodItem> data = mapper.readValue(
                    file,
                    new TypeReference<List<FoodItem>>() {}
            );

            inventory.clear();
            if (data != null) {
                inventory.addAll(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from " + FILE_PATH, e);
        }
    }

    private void saveToFile() {
        try {
            mapper.writeValue(new File(FILE_PATH), inventory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to " + FILE_PATH, e);
        }
    }

    public void addFood(String name, String expiryDateStr, String category) {
        addFood(name, expiryDateStr, category, "Fridge");
    }

    public void addFood(String name, String expiryDateStr, String category, String storageArea) {
        FoodItem newItem = new FoodItem(
                String.valueOf(System.currentTimeMillis()),
                name,
                LocalDate.parse(expiryDateStr),
                category,
                storageArea
        );

        inventory.add(newItem);
        saveToFile();
    }

    public List<FoodItem> getInventory() {
        return inventory;
    }

    public List<FoodItem> getFilteredAndSortedItems(String category, String chip, String sortBy, String q) {
        return inventory.stream()
                .filter(item -> {
                    if (category == null || category.isBlank() || category.equalsIgnoreCase("All")) {
                        return true;
                    }
                    return item.getCategory() != null && item.getCategory().equalsIgnoreCase(category);
                })
                .filter(item -> {
                    if (q == null || q.isBlank()) {
                        return true;
                    }
                    return item.getName() != null &&
                            item.getName().toLowerCase().contains(q.toLowerCase());
                })
                .filter(item -> {
                    if (chip == null || chip.isBlank()) {
                        return true;
                    }
                    if (chip.equalsIgnoreCase("expiry")) {
                        return item.getDaysLeft() >= 0 && item.getDaysLeft() <= 3;
                    }
                    if (chip.equalsIgnoreCase("new")) {
                        return item.getDaysLeft() > 0 && item.getDaysLeft() <= 2;
                    }
                    return true;
                })
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }

    private Comparator<FoodItem> getComparator(String sortBy) {
        if (sortBy == null || sortBy.isBlank() || sortBy.equalsIgnoreCase("expiry")) {
            return Comparator.comparing(FoodItem::getExpiryDate);
        }

        if (sortBy.equalsIgnoreCase("name")) {
            return Comparator.comparing(
                    item -> item.getName() == null ? "" : item.getName().toLowerCase()
            );
        }

        if (sortBy.equalsIgnoreCase("category")) {
            return Comparator.comparing(
                    item -> item.getCategory() == null ? "" : item.getCategory().toLowerCase()
            );
        }

        return Comparator.comparing(FoodItem::getExpiryDate);
    }

    public Map<String, Long> getStorageSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("Fridge", 0L);
        summary.put("Freezer", 0L);
        summary.put("Pantry", 0L);

        for (FoodItem item : inventory) {
            String area = item.getStorageArea();
            summary.put(area, summary.getOrDefault(area, 0L) + 1);
        }

        return summary;
    }

    public long getExpiringSoonCount() {
        return inventory.stream()
                .filter(item -> item.getDaysLeft() >= 0 && item.getDaysLeft() <= 3)
                .count();
    }
}