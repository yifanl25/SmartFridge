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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 手机看「冰箱里有什么」/ HTTP fridge list for Flutter.
 * <p>Now 现在: GET 全部可见食材；POST 用名字加一行（走 {@link InventoryController#addItem(String)}）。</p>
 * <p>Missing 还缺: 按类筛选、按到期排序、按添加时间排序 — Java 里 InventoryController <strong>已经有</strong>方法，只是没做成 HTTP。
 * 打字联想在 {@link CatalogApiController}。</p>
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryApiController {

    private final InventoryController inventoryController;

    public InventoryApiController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    // 给做 HTTP 的同学 / For whoever adds URLs here:
    // 要做：把「筛选、排序」也做成网页能调 — 直接调用下面这个 inventoryController 里<strong>已经写好</strong>的方法。
    // To do: expose filter + sort over HTTP — call the <strong>existing</strong> methods on inventoryController.
    // 记住 / Remember: 只在内存里跑一圈 demo，关程序就清空 / one session in RAM, no save to disk as "database".
    // 不要给食材加「仓库位置」字段 / Do not add storageLocation on FoodItem.
    // 食材类 FoodCategory 和菜谱类 RecipeCategory 是两个东西 / Keep food vs recipe categories separate.
    // INSERT YOUR CODE HERE

    @GetMapping
    public List<FoodItemResponse> list() {
        return inventoryController.getVisibleItems().stream()
                .map(FoodItemResponse::from)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<FoodItemResponse> add(@RequestBody AddFoodRequest body) {
        if (body == null || body.getFoodName() == null || body.getFoodName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        FoodItem added = inventoryController.addItem(body.getFoodName().trim());
        return ResponseEntity.ok(FoodItemResponse.from(added));
    }

    // 作业 ProductExpectations 说：要把「做好的清单」存成 json 或 xml 或 csv 里的一种。
    // Course doc: save a built list as json OR xml OR csv.
    // 控制台能 export-grocery 存购物单 JSON；网页这边如果要「下载清单」，再加接口 / Console has JSON export; HTTP side not yet.
    // 仍然不要做成数据库 / Still not a real database.
    // INSERT YOUR CODE HERE
}
