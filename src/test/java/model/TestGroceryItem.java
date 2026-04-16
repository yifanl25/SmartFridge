package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：购物清单行模型（对应 {@link GroceryItem}；金额汇总由服务层处理）。
 */
public class TestGroceryItem {
    /**
     * 测试功能：构造、getter，以及数量/已采购的可变状态。
     * 验证点：与 UI 加减、勾选行为一致。
     * <p>
     * 对应源码 / Maps to: {@link GroceryItem#GroceryItem(String, String, FoodCategory, int, double, boolean)}，
     * {@link GroceryItem#getId()}, {@link GroceryItem#getName()}, {@link GroceryItem#getCategory()}, {@link GroceryItem#getQuantity()},
     * {@link GroceryItem#getPrice()}, {@link GroceryItem#isCollected()}, {@link GroceryItem#setCollected(boolean)}, {@link GroceryItem#setQuantity(int)}
     */
    @Test
    void testConstructorAndStateChanges() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        GroceryItem item = new GroceryItem("g1", "Milk", category, 2, 3.5, false);

        assertEquals("g1", item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(category, item.getCategory());
        assertEquals(2, item.getQuantity());
        assertEquals(3.5, item.getPrice());
        assertFalse(item.isCollected());

        item.setCollected(true);
        item.setQuantity(5);
        assertTrue(item.isCollected());
        assertEquals(5, item.getQuantity());
    }
}
