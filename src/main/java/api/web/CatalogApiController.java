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
 * 加食材时的「打字联想」/ Type-ahead search for add-item.
 * <p>Now 现在: GET ?q= 前缀，调 foodCatalog.searchSuggestions。</p>
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogApiController {

    private final IFoodCatalog foodCatalog;

    public CatalogApiController(IFoodCatalog foodCatalog) {
        this.foodCatalog = foodCatalog;
    }

    // 以后可加：按 id 查一条目录、查默认保质期… IFoodCatalog 里还有别的方法，PRD 要再加 URL。
    // Later: lookup by catalog id, expiry days, etc. — only if PRD asks. 只读、不建数据库 / Read-only, no DB.
    // INSERT YOUR CODE HERE

    @GetMapping("/suggestions")
    public ResponseEntity<List<FoodCatalogEntry>> suggestions(@RequestParam("q") String prefix) {
        if (prefix == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodCatalog.searchSuggestions(prefix));
    }
}
