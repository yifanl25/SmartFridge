package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TDD：库存食材项模型（对应 {@link FoodItem}，「新入库」与「临期」规则）。
 */
public class TestFoodItem {
    /**
     * 测试功能：构造与访问器；「新」标签（创建日为今天时视为新）。
     * 验证点：字段一致且 {@link FoodItem#isNew()} 为 true。
     * <p>
     * 对应源码 / Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，
     * {@link FoodItem#getId()}, {@link FoodItem#getName()}, {@link FoodItem#getCategory()}, {@link FoodItem#getQuantity()},
     * {@link FoodItem#getUnit()}, {@link FoodItem#isNew()}
     */
    @Test
    void testConstructorAndGettersAndNew() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem item = new FoodItem(
                "f1",
                "Milk",
                category,
                1,
                "pcs",
                today,
                LocalDate.now().plusDays(7).toString());

        assertEquals("f1", item.getId());
        assertEquals("Milk", item.getName());
        assertEquals(category, item.getCategory());
        assertEquals(1, item.getQuantity());
        assertEquals("pcs", item.getUnit());
        assertTrue(item.isNew());
    }

    /**
     * 测试功能：「紧急/临期」标签——过期日为当天。
     * 验证点：{@link FoodItem#isUrgent()} 为 true。
     * <p>
     * 对应源码 / Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，{@link FoodItem#isUrgent()}
     */
    @Test
    void testIsUrgentWhenExpiryIsToday() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem urgent = new FoodItem("f2", "Milk", category, 1, "pcs", today, today);
        assertTrue(urgent.isUrgent());
    }

    /**
     * 测试功能：过期日晚于今天时不标为紧急。
     * 验证点：{@link FoodItem#isUrgent()} 为 false。
     * <p>
     * 对应源码 / Maps to: {@link FoodItem#FoodItem(String, String, FoodCategory, int, String, String, String)}，{@link FoodItem#isUrgent()}
     */
    @Test
    void testIsNotUrgentWhenExpiryIsFuture() {
        FoodCategory category = new FoodCategory("c1", "Dairy", "milk");
        String today = LocalDate.now().toString();
        FoodItem safe = new FoodItem(
                "f3", "Milk", category, 1, "pcs", today, LocalDate.now().plusDays(3).toString());
        assertFalse(safe.isUrgent());
    }
}
