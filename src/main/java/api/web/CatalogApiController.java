package api.web;

import controller.CatalogController;
import model.FoodCatalogEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HTTP entry layer for read-only catalog endpoints used by Flutter.
 * <p>
 * Spring request mapping lives here; actual catalog lookups are delegated to the internal
 * {@link controller.CatalogController}.
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogApiController {

    private final CatalogController catalogController;

    public CatalogApiController(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<FoodCatalogEntry>> suggestions(@RequestParam("q") String prefix) {
        if (prefix == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(catalogController.searchSuggestions(prefix));
    }
}
