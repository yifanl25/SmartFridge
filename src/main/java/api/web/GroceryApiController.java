package api.web;

import api.dto.GroceryAddRequest;
import controller.GroceryController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.GroceryItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.IFoodCatalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 手机上的「购物清单」/ HTTP shopping list for Flutter.
 * <p>Now 现在: 看清单、加一行、勾选买了没、改数量、结账、看小计+税+总价。</p>
 * <p>Missing 还缺: 删一行（Java 里 deleteItem 有了，网页还没 DELETE）；可能还要「导出清单」.</p>
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

    // 要做：加一个 DELETE，用 id 删掉购物车里的一行 — 调 groceryController.deleteItem 就行。
    // To do: HTTP DELETE by line id → groceryController.deleteItem. 找不到就返回 404 / Return 404 if missing id.
    // 还是内存 demo，别加数据库 / Still in-memory demo, no DB.
    // INSERT YOUR CODE HERE

    @GetMapping("/items")
    public List<GroceryItem> items() {
        return groceryController.getItems();
    }

    @PostMapping("/items")
    public ResponseEntity<GroceryItem> addLine(@RequestBody GroceryAddRequest body) {
        if (body == null || body.getFoodName() == null || body.getFoodName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<FoodCatalogEntry> resolved = foodCatalog.resolveEntry(body.getFoodName().trim());
        FoodCatalogEntry entry = resolved.orElseGet(
                () -> foodCatalog.searchSuggestions(body.getFoodName().trim()).stream()
                        .findFirst()
                        .orElse(null));
        if (entry == null) {
            FoodCategory misc = new FoodCategory("misc", "Misc", "box");
            entry = new FoodCatalogEntry(body.getFoodName().trim(), 3, misc);
        }
        FoodCategory cat = entry.getCategory();
        String id = UUID.randomUUID().toString();
        String name = foodCatalog.canonicalFoodName(body.getFoodName().trim());
        int qty = Math.max(0, body.getQuantity());
        double price = body.getPrice() >= 0 ? body.getPrice() : 0.0;
        GroceryItem line = new GroceryItem(id, name, cat, qty, price, false);
        groceryController.addLine(line);
        return ResponseEntity.ok(line);
    }

    // 可选：如果作业要网页也能「下载购物单」——控制台已有 JSON 导出；这里可以照抄思路做成 HTTP 下载。
    // Optional: HTTP download of grocery list like console JSON export. 计价规则别在这里乱改，去 GroceryService / Totals stay in GroceryService.
    // INSERT YOUR CODE HERE

    @PatchMapping("/items/{id}/collected")
    public ResponseEntity<GroceryItem> toggleCollected(@PathVariable String id) {
        return ResponseEntity.ok(groceryController.toggleCollected(id));
    }

    @PatchMapping("/items/{id}/quantity")
    public ResponseEntity<GroceryItem> updateQuantity(
            @PathVariable String id,
            @RequestParam("delta") int delta) {
        return ResponseEntity.ok(groceryController.updateQuantity(id, delta));
    }

    @PostMapping("/checkout")
    public void checkout() {
        // 注意 / Heads-up: 现在结账会<strong>整局清空</strong>（偏好+冰箱+推荐+购物）——这是 FridgeBeansConfig 里接的 SessionReset。
        // Warning: checkout <strong>clears the whole session</strong> today (see FridgeBeansConfig + SessionReset).
        // 如果以后 PRD 说「只清空购物车」：要改 GroceryController 外面传进去的那个 Runnable，先跟全队说好再改 /
        // If PRD later says "only clear cart", change the Runnable wiring — talk to team first.
        // INSERT YOUR CODE HERE
        groceryController.checkout();
    }

    @GetMapping("/totals")
    public Map<String, Double> totals() {
        double sub = groceryController.calculateSubtotal();
        double tax = groceryController.calculateTax(sub);
        double total = groceryController.calculateTotal(sub, tax);
        Map<String, Double> m = new HashMap<>();
        m.put("subtotal", sub);
        m.put("tax", tax);
        m.put("total", total);
        return m;
    }
}
