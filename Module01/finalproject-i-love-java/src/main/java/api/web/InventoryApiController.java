package api.web;

import api.dto.AddFoodRequest;
import api.dto.FoodItemResponse;
import controller.InventoryController;
import model.FoodItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles HTTP requests for the fridge inventory.
 *
 * This controller supports:
 * - listing inventory items
 * - filtering by category
 * - sorting by expiry or created time
 * - returning a simple inventory summary
 * - adding one food item
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryApiController {

    private final InventoryController inventoryController;

    public InventoryApiController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    /**
     * Main inventory endpoint.
     *
     * Supported query parameters:
     * - category
     * - sort=expiry
     * - sort=created
     * - sort=newest
     */
    @GetMapping
    public List<FoodItemResponse> list(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false) String sort) {

        List<FoodItem> items = new ArrayList<>(inventoryController.getVisibleItems());

        if (category != null && !category.isBlank()) {
            String targetCategory = category.trim().toLowerCase();
            List<FoodItem> filtered = new ArrayList<>();

            for (FoodItem item : items) {
                String itemCategory = item.getCategory().getName().toLowerCase();
                if (itemCategory.equals(targetCategory)) {
                    filtered.add(item);
                }
            }

            items = filtered;
        }

        if (sort != null && !sort.isBlank()) {
            String mode = sort.trim().toLowerCase();

            if (mode.equals("expiry")) {
                items.sort(
                        Comparator.comparing(FoodItem::getExpiryDate)
                                .thenComparing(Comparator.comparing(FoodItem::getCreatedAt).reversed())
                );
            } else if (mode.equals("created") || mode.equals("newest")) {
                items.sort(Comparator.comparing(FoodItem::getCreatedAt).reversed());
            }
        }

        List<FoodItemResponse> result = new ArrayList<>();
        for (FoodItem item : items) {
            result.add(FoodItemResponse.from(item));
        }

        return result;
    }

    /**
     * Legacy filter endpoint kept for compatibility.
     */
    @GetMapping("/filter")
    public List<FoodItemResponse> filter(@RequestParam("category") String category) {
        return list(category, null);
    }

    /**
     * Legacy sort endpoint kept for compatibility.
     */
    @GetMapping("/sort")
    public List<FoodItemResponse> sort(@RequestParam("mode") String mode) {
        return list(null, mode);
    }

    /**
     * Returns a simple summary of the current inventory.
     */
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        List<FoodItem> items = inventoryController.getVisibleItems();

        int urgentCount = 0;
        int newCount = 0;
        List<String> categories = new ArrayList<>();

        for (FoodItem item : items) {
            if (item.isUrgent()) {
                urgentCount++;
            }

            if (item.isNew()) {
                newCount++;
            }

            String categoryName = item.getCategory().getName();
            if (!categories.contains(categoryName)) {
                categories.add(categoryName);
            }
        }

        categories.sort(String::compareTo);

        Map<String, Object> result = new HashMap<>();
        result.put("count", items.size());
        result.put("urgentCount", urgentCount);
        result.put("newCount", newCount);
        result.put("categories", categories);

        return result;
    }

    @PostMapping
    public ResponseEntity<FoodItemResponse> add(@RequestBody AddFoodRequest body) {
        if (body == null || body.getFoodName() == null || body.getFoodName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        FoodItem added = inventoryController.addItem(body.getFoodName().trim());
        return ResponseEntity.ok(FoodItemResponse.from(added));
    }
}
