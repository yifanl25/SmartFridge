package controller;

import model.GroceryItem;
import service.IGroceryService;

import java.util.List;

/**
 * 这个 controller 是 grocery 模块中间那一层。
 *
 * 大白话：
 * - API 层不要直接碰 service 细节
 * - 所以这里当一个中间转发层
 * - 上面接 API，下面接 IGroceryService
 *
 * 这样整体分层还是保持你原本的 MVC 结构。
 */
public class GroceryController {
    // 真正管购物清单逻辑的是 service；controller 主要负责转发。
    private final IGroceryService groceryService;

    // 这是 checkout 时要执行的“收尾动作”。
    // 默认只是清空 grocery；
    // 但如果以后你要把库存、偏好、推荐一起重置，
    // 也可以把更大的 reset 动作塞进来。
    private final Runnable onCheckoutLoopEnd;

    /**
     * 默认构造：checkout 只做 grocery 自己的清空。
     */
    public GroceryController(IGroceryService groceryService) {
        this(groceryService, groceryService::checkout);
    }

    /**
     * 自定义构造：允许外部传一个更完整的 checkout 收尾逻辑。
     */
    public GroceryController(IGroceryService groceryService, Runnable onCheckoutLoopEnd) {
        this.groceryService = groceryService;
        this.onCheckoutLoopEnd = onCheckoutLoopEnd;
    }

    /**
     * 取当前购物清单。
     */
    public List<GroceryItem> getItems() {
        return groceryService.getItems();
    }

    /**
     * 加一条购物项。
     */
    public void addLine(GroceryItem item) {
        groceryService.addLine(item);
    }

    /**
     * 按分类筛选购物项。
     */
    // ===== teammate note =====
    // controller 这里只做转发，不要把复杂业务规则塞进来。
    // insert your code here only if the service signature changes
    public List<GroceryItem> filterByCategory(String categoryName) {
        return groceryService.filterByCategory(categoryName);
    }

    /**
     * 按名字搜索购物项。
     */
    // ===== teammate note =====
    // controller 这里只做转发，不要把复杂搜索逻辑写在这里。
    // insert your code here only if the service signature changes
    public List<GroceryItem> searchByName(String keyword) {
        return groceryService.searchByName(keyword);
    }

    /**
     * 勾选 / 取消勾选某个购物项是否已买。
     */
    public GroceryItem toggleCollected(String itemId) {
        return groceryService.toggleCollected(itemId);
    }

    /**
     * 按增量修改数量。
     */
    public GroceryItem updateQuantity(String itemId, int delta) {
        return groceryService.updateQuantity(itemId, delta);
    }

    /**
     * 删除一条购物项。
     */
    public void deleteItem(String itemId) {
        groceryService.deleteItem(itemId);
    }

    /**
     * 算小计。
     */
    public double calculateSubtotal() {
        return groceryService.calculateSubtotal();
    }

    /**
     * 算税。
     */
    public double calculateTax(double subtotal) {
        return groceryService.calculateTax(subtotal);
    }

    /**
     * 算总价。
     */
    public double calculateTotal(double subtotal, double tax) {
        return groceryService.calculateTotal(subtotal, tax);
    }

    /**
     * 执行 checkout。
     *
     * 这里不是直接写死成某一种 reset，
     * 而是跑构造时传进来的 Runnable，
     * 这样以后要换结账后的行为会更灵活。
     */
    public void checkout() {
        onCheckoutLoopEnd.run();
    }
}
