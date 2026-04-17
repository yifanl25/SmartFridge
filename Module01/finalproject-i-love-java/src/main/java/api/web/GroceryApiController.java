package api.web;

import api.dto.GroceryAddRequest;
import controller.GroceryController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.GroceryItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IFoodCatalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Handles HTTP requests for the grocery list.
 */
@RestController
@RequestMapping("/api/grocery")
public class GroceryApiController {

    private final GroceryController groceryController;
    private final IFoodCatalog foodCatalog;

    public GroceryApiController(GroceryController groceryController, IFoodCatalog foodCatalog) {
        this.groceryController = groceryController;
        this.foodCatalog = foodCatalog;
    }

    /**
     * Returns the grocery list.
     *
     * Supported query parameters:
     * - category
     * - search
     */
    @GetMapping("/items")
    public List<GroceryItem> items(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "search", required = false) String search) {

        List<GroceryItem> base;

        if (category == null || category.isBlank()) {
            base = groceryController.getItems();
        } else {
            base = groceryController.filterByCategory(category);
        }

        if (search == null || search.isBlank()) {
            return base;
        }

        String keyword = search.trim().toLowerCase();
        List<GroceryItem> result = new ArrayList<>();

        for (GroceryItem item : base) {
            if (item.getName().toLowerCase().contains(keyword)) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Adds one grocery item manually.
     */
    @PostMapping("/items")
    public ResponseEntity<GroceryItem> addLine(@RequestBody GroceryAddRequest body) {
        if (body == null || body.getFoodName() == null || body.getFoodName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String rawName = body.getFoodName().trim();

        Optional<FoodCatalogEntry> resolved = foodCatalog.resolveEntry(rawName);
        FoodCatalogEntry entry;

        if (resolved.isPresent()) {
            entry = resolved.get();
        } else {
            List<FoodCatalogEntry> suggestions = foodCatalog.searchSuggestions(rawName);
            if (suggestions.isEmpty()) {
                FoodCategory misc = new FoodCategory("misc", "Misc", "box");
                entry = new FoodCatalogEntry(rawName, 3, misc);
            } else {
                entry = suggestions.get(0);
            }
        }

        String id = UUID.randomUUID().toString();
        String name = foodCatalog.canonicalFoodName(rawName);
        FoodCategory category = entry.getCategory();

        int quantity = Math.max(0, body.getQuantity());
        double price = body.getPrice() >= 0 ? body.getPrice() : 0.0;

        GroceryItem line = new GroceryItem(id, name, category, quantity, price, false);
        groceryController.addLine(line);

        return ResponseEntity.ok(line);
    }

    /**
     * Deletes one grocery item.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable String id) {
        List<GroceryItem> items = groceryController.getItems();
        boolean exists = false;

        for (GroceryItem item : items) {
            if (item.getId().equals(id)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        groceryController.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toggles whether one item is collected.
     */
    @PatchMapping("/items/{id}/collected")
    public ResponseEntity<GroceryItem> toggleCollected(@PathVariable String id) {
        GroceryItem updated = groceryController.toggleCollected(id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Updates quantity by delta.
     */
    @PatchMapping("/items/{id}/quantity")
    public ResponseEntity<GroceryItem> updateQuantity(
            @PathVariable String id,
            @RequestParam("delta") int delta) {
        GroceryItem updated = groceryController.updateQuantity(id, delta);
        return ResponseEntity.ok(updated);
    }

    /**
     * Checks out the grocery list.
     */
    @PostMapping("/checkout")
    public void checkout() {
        groceryController.checkout();
    }

    /**
     * Returns subtotal, tax, and total.
     */
    @GetMapping("/totals")
    public Map<String, Double> totals() {
        double subtotal = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(subtotal);
        double total = groceryController.calculateTotal(subtotal, tax);

        Map<String, Double> result = new HashMap<>();
        result.put("subtotal", subtotal);
        result.put("tax", tax);
        result.put("total", total);

        return result;
    }
}