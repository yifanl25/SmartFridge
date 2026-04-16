package controller;

import model.FoodCategory;
import model.GroceryItem;
import service.GroceryService;
import service.IGroceryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：购物清单控制器委托（对应 {@link GroceryController} → {@link IGroceryService}）。
 */
public class TestGroceryController {
    private GroceryController groceryController;
    private IGroceryService groceryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        groceryService = new GroceryService(List.of(new GroceryItem("1", "Milk", cat, 2, 3.0, false)));
        groceryController = new GroceryController(groceryService);
    }

    /**
     * 测试功能：切换行「已采购」状态。
     * 验证点：委托服务并反映在新状态上。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#toggleCollected(String)} → {@link GroceryService#toggleCollected(String)}
     */
    @Test
    void testToggleCollectedDelegatesToGroceryService() {
        assertEquals(true, groceryController.toggleCollected("1").isCollected());
    }

    /**
     * 测试功能：调整购物行数量。
     * 验证点：委托服务后数量正确。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#updateQuantity(String, int)} → {@link GroceryService#updateQuantity(String, int)}
     */
    @Test
    void testUpdateQuantityDelegatesToGroceryService() {
        assertEquals(3, groceryController.updateQuantity("1", 1).getQuantity());
    }

    /**
     * 测试功能：删除购物行。
     * 验证点：底层服务列表被清空。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#deleteItem(String)} → {@link GroceryService#deleteItem(String)}
     */
    @Test
    void testDeleteItemDelegatesToGroceryService() {
        groceryController.deleteItem("1");
        assertEquals(0, groceryService.getItems().size());
    }

    /**
     * 测试功能：计算小计（仅已勾选行）。
     * 验证点：委托服务，数值与 PRD 示例一致。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#calculateSubtotal()} → {@link GroceryService#calculateSubtotal()}
     */
    @Test
    void testCalculateSubtotalDelegatesToGroceryService() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        IGroceryService svc = new GroceryService(List.of(new GroceryItem("1", "Milk", cat, 2, 3.0, true)));
        GroceryController ctrl = new GroceryController(svc);
        assertEquals(6.0, ctrl.calculateSubtotal());
    }

    /**
     * 测试功能：按给定小计计算税额。
     * 验证点：委托服务，8% 税率。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#calculateTax(double)} → {@link GroceryService#calculateTax(double)}
     */
    @Test
    void testCalculateTaxDelegatesToGroceryService() {
        assertEquals(0.48, groceryController.calculateTax(6.0), 0.0001);
    }

    /**
     * 测试功能：小计 + 税 = 总额。
     * 验证点：委托服务，结果正确。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#calculateTotal(double, double)} → {@link GroceryService#calculateTotal(double, double)}
     */
    @Test
    void testCalculateTotalDelegatesToGroceryService() {
        assertEquals(6.48, groceryController.calculateTotal(6.0, 0.48), 0.0001);
    }

    /**
     * 测试功能：结账（本控制器默认仅清空购物服务；完整会话重置由带 Runnable 的构造协调）。
     * 验证点：购物列表被清空。
     * <p>
     * 对应源码 / Maps to: {@link GroceryController#checkout()} → {@link GroceryService#checkout()}
     */
    @Test
    void testCheckoutDelegatesToGroceryService() {
        groceryController.checkout();
        assertEquals(0, groceryService.getItems().size());
    }
}
