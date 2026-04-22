package api.web;

import api.dto.GroceryAddRequest;
import controller.CatalogController;
import controller.GroceryController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import model.GroceryItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * HTTP entry layer for grocery endpoints used by the Flutter frontend.
 * <p>
 * Spring routing and request/response handling stay here. Grocery state changes delegate to
 * {@link GroceryController}, and catalog-backed name resolution delegates to {@link CatalogController}.
 */
@RestController
@RequestMapping("/api/grocery")
public class GroceryApiController {

    private final CatalogController catalogController;
    private final GroceryController groceryController;

    public GroceryApiController(CatalogController catalogController, GroceryController groceryController) {
        this.catalogController = catalogController;
        this.groceryController = groceryController;
    }

    /**
     * 返回购物清单。
     *
     * 支持两个可选 query：
     * - category：先按分类过滤
     * - search：再按名字关键字过滤
     */
    @GetMapping("/items")
    // ===== teammate note =====
    // 这里是 grocery list 的查询入口。
    // 后面如果要补 sort、分页、只看已买/未买，也是在这里继续加 query 逻辑。
    // insert your code here: add more filters like sort/collectedOnly/pagination
    public List<GroceryItem> items(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "search", required = false) String search) {

        // 先处理分类。
        List<GroceryItem> base = (category == null || category.isBlank())
                ? groceryController.getItems()
                : groceryController.filterByCategory(category);

        // 如果没有 search，就直接把当前结果返回。
        if (search == null || search.isBlank()) {
            return base;
        }

        // 再按名字做一次包含匹配。
        String needle = search.trim().toLowerCase();
        return base.stream()
                .filter(item -> item.getName().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * 手动加一条 grocery item。
     *
     * 这里会尽量先走 food catalog：
     * - 能识别就用标准名字和标准分类
     * - 实在识别不了，就先塞进 Misc
     */
    @PostMapping("/items")
    // ===== teammate note =====
    // 这里处理“手动加购物项”。
    // 现在是尽量走 catalog，不行就丢到 Misc。
    // 如果你们后面决定严格禁止自由输入，或者要校验 price/unit，就从这里改。
    // insert your code here: tighten validation or align this endpoint with final PRD rules
    public ResponseEntity<GroceryItem> addLine(@RequestBody GroceryAddRequest body) {
        if (body == null || body.getFoodName() == null || body.getFoodName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<FoodCatalogEntry> resolved = catalogController.resolveEntry(body.getFoodName().trim());

        // 精确解析不到，再退一步用 suggestion。
        FoodCatalogEntry entry = resolved.orElseGet(
                () -> catalogController.searchSuggestions(body.getFoodName().trim()).stream()
                        .findFirst()
                        .orElse(null));

        // 再找不到，就临时归到 Misc。
        if (entry == null) {
            FoodCategory misc = new FoodCategory("misc", "Misc", "box");
            entry = new FoodCatalogEntry(body.getFoodName().trim(), 3, misc);
        }

        FoodCategory cat = entry.getCategory();
        String id = UUID.randomUUID().toString();
        String name = catalogController.canonicalFoodName(body.getFoodName().trim());

        // 数量和价格做最基本保护：
        // - quantity 最少 0
        // - price 不能是负数
        int qty = Math.max(0, body.getQuantity());
        double price = body.getPrice() >= 0 ? body.getPrice() : 0.0;

        GroceryItem line = new GroceryItem(id, name, cat, qty, price, false);
        groceryController.addLine(line);
        return ResponseEntity.ok(line);
    }

    /**
     * 删除一条购物项。
     *
     * 删除前先确认这条 id 真的存在，
     * 不然就回 404。
     */
    @DeleteMapping("/items/{id}")
    // ===== teammate note =====
    // 这里是删除购物项。
    // 如果后面要做“软删除 / undo / 批量删除”，可以从这个方法扩。
    // insert your code here: extend delete behavior if needed
    public ResponseEntity<Void> deleteLine(@PathVariable String id) {
        boolean exists = groceryController.getItems().stream().anyMatch(i -> i.getId().equals(id));
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        groceryController.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 切换某一项是否已买。
     */
    @PatchMapping("/items/{id}/collected")
    public ResponseEntity<GroceryItem> toggleCollected(@PathVariable String id) {
        return ResponseEntity.ok(groceryController.toggleCollected(id));
    }

    /**
     * 按 delta 修改数量。
     *
     * 例子：
     * - delta=1 代表加 1
     * - delta=-1 代表减 1
     */
    @PatchMapping("/items/{id}/quantity")
    // ===== teammate note =====
    // 这里现在是按 delta 改数量。
    // 如果前端最后改成“直接传目标数量”而不是 +1/-1，就要改这里和 controller/service 一起对齐。
    // insert your code here: support setQuantity mode if UI changes
    public ResponseEntity<GroceryItem> updateQuantity(
            @PathVariable String id,
            @RequestParam("delta") int delta) {
        return ResponseEntity.ok(groceryController.updateQuantity(id, delta));
    }

    /**
     * 结账。
     *
     * 当前动作最终会走到 groceryController.checkout()。
     */
    @PostMapping("/checkout")
    public void checkout() {
        groceryController.checkout();
    }

    /**
     * 返回金额汇总。
     *
     * 前端常用这三个值：
     * - subtotal
     * - tax
     * - total
     */
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
