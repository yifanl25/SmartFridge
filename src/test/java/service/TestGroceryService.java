package service;

import model.FoodCategory;
import model.GroceryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：购物清单会话状态与计价规则（对应 {@link GroceryService}，PRD 小计/税/结账）。
 */
public class TestGroceryService {
    private GroceryService groceryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        groceryService = new GroceryService(List.of(
                new GroceryItem("1", "Milk", cat, 2, 3.0, false)));
    }

    /**
     * 测试功能：读取当前购物清单全部行。
     * 验证点：与构造时注入条数一致。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#getItems()}
     */
    @Test
    void testGetItemsReturnsCurrentGroceryItems() {
        assertEquals(1, groceryService.getItems().size());
    }

    /**
     * 测试功能：向购物清单追加一行（缺失食材导入等场景）。
     * 验证点：{@code addLine} 后条数 +1。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#addLine(GroceryItem)}、{@link GroceryService#getItems()}
     */
    @Test
    void testAddLineAppendsRow() {
        FoodCategory cat = new FoodCategory("c2", "Produce", "leaf");
        groceryService.addLine(new GroceryItem("2", "Spinach", cat, 1, 1.5, false));
        assertEquals(2, groceryService.getItems().size());
    }

    /**
     * 测试功能：切换「已采购」勾选状态。
     * 验证点：仅影响该行 collected 标志。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#toggleCollected(String)}
     */
    @Test
    void testToggleCollectedUpdatesState() {
        assertEquals(true, groceryService.toggleCollected("1").isCollected());
    }

    /**
     * 测试功能：按增量修改购物行数量。
     * 验证点：数量在合法范围内更新。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#updateQuantity(String, int)}
     */
    @Test
    void testUpdateQuantityChangesAmount() {
        assertEquals(3, groceryService.updateQuantity("1", 1).getQuantity());
    }

    /**
     * 测试功能：从购物清单删除一行。
     * 验证点：删除后列表为空。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#deleteItem(String)}、{@link GroceryService#getItems()}
     */
    @Test
    void testDeleteItemRemovesItem() {
        groceryService.deleteItem("1");
        assertEquals(0, groceryService.getItems().size());
    }

    /**
     * 测试功能：小计 = Σ(单价×数量)，且仅统计已勾选「已采购」的行。
     * 验证点：2×3.0=6.0。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#calculateSubtotal()}
     */
    @Test
    void testCalculateSubtotalUsesCollectedItems() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        GroceryService svc = new GroceryService(List.of(
                new GroceryItem("1", "Milk", cat, 2, 3.0, true)));
        assertEquals(6.0, svc.calculateSubtotal());
    }

    /**
     * 测试功能：税额 = 小计 × 8%。
     * 验证点：6.0 → 0.48。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#calculateTax(double)}
     */
    @Test
    void testCalculateTaxUsesEightPercent() {
        assertEquals(0.48, groceryService.calculateTax(6.0), 0.0001);
    }

    /**
     * 测试功能：应付总额 = 小计 + 税。
     * 验证点：6.0 + 0.48 = 6.48。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#calculateTotal(double, double)}
     */
    @Test
    void testCalculateTotalAddsSubtotalAndTax() {
        assertEquals(6.48, groceryService.calculateTotal(6.0, 0.48), 0.0001);
    }

    /**
     * 测试功能：结账时清空本模块购物清单（完整会话重置由上层协调）。
     * 验证点：{@code checkout} 后条数为 0。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#checkout()}、{@link GroceryService#getItems()}
     */
    @Test
    void testCheckoutClearsGroceryItems() {
        groceryService.checkout();
        assertEquals(0, groceryService.getItems().size());
    }

    /**
     * 测试功能：会话重置时直接清空购物清单。
     * 验证点：{@code clearGrocery} 后无行。
     * <p>
     * 对应源码 / Maps to: {@link GroceryService#clearGrocery()}
     */
    @Test
    void testClearGroceryRemovesAllItems() {
        groceryService.clearGrocery();
        assertEquals(0, groceryService.getItems().size());
    }
}
