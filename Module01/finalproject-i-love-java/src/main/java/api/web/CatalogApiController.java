package api.web;

import model.FoodCatalogEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.IFoodCatalog;

import java.util.List;

/**
 * Handles catalog suggestion requests for the add-item flow.
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogApiController {

    private final IFoodCatalog foodCatalog;

    public CatalogApiController(IFoodCatalog foodCatalog) {
        this.foodCatalog = foodCatalog;
    }

    /**
     * Returns catalog suggestions for the given prefix.
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<FoodCatalogEntry>> suggestions(@RequestParam("q") String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        List<FoodCatalogEntry> suggestions = foodCatalog.searchSuggestions(prefix.trim());
        return ResponseEntity.ok(suggestions);
    }
}