package service;

import model.FoodCatalogEntry;
import model.FoodCategory;
import model.FoodItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：会话内库存的增删查改与排序（对应 {@link InventoryService}）。
 */
public class TestInventoryService {
    private InventoryService inventoryService;
    private IFoodCatalog foodCatalog;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        foodCatalog = new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat)));
        inventoryService = new InventoryService(foodCatalog);
    }

    /**
     * 测试功能：查询当前会话全部库存项。
     * 验证点：初始为空列表。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#getAllItems()}
     */
    @Test
    void testGetAllItemsReturnsCurrentInventory() {
        assertEquals(0, inventoryService.getAllItems().size());
    }

    /**
     * 测试功能：按食材名添加库存（目录解析、默认数量等）。
     * 验证点：名称与目录一致时成功加入并可读出。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#addItem(String)}
     */
    @Test
    void testAddItemCreatesFoodItemFromCatalog() {
        assertEquals("Milk", inventoryService.addItem("Milk").getName());
    }

    /**
     * 测试功能：按食材分类（{@link FoodCategory}）筛选库存。
     * 验证点：仅返回该分类下的项。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#filterByCategory(String)}（先 {@link InventoryService#addItem(String)}）
     */
    @Test
    void testFilterByCategoryReturnsMatchingItems() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.filterByCategory("Dairy").size());
    }

    /**
     * 测试功能：按过期日排序（临期优先展示）。
     * 验证点：排序结果条数与源一致（单条场景）。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#sortByExpiry()}
     */
    @Test
    void testSortByExpiryOrdersUrgentFirst() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.sortByExpiry().size());
    }

    /**
     * 测试功能：按入库/创建时间排序（新到旧）。
     * 验证点：排序结果非空且条数正确。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#sortByCreatedTime()}
     */
    @Test
    void testSortByCreatedTimeOrdersNewestFirst() {
        inventoryService.addItem("Milk");
        assertEquals(1, inventoryService.sortByCreatedTime().size());
    }

    /**
     * 测试功能：清空会话库存（结账/循环结束 PRD）。
     * 验证点：清空后 {@code getAllItems()} 为 0。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#clearInventory()}、{@link InventoryService#getAllItems()}
     */
    @Test
    void testClearInventoryRemovesAllItems() {
        inventoryService.addItem("Milk");
        inventoryService.clearInventory();
        assertEquals(0, inventoryService.getAllItems().size());
    }

    /**
     * 测试功能：批量追加预构建的 {@link FoodItem}（演示种子 / data.json 灌入）。
     * 验证点：{@code addAllItems} 后总数累加。
     * <p>
     * 对应源码 / Maps to: {@link InventoryService#addAllItems(List)}、{@link InventoryService#getAllItems()}
     */
    @Test
    void testAddAllItemsAppendsBatch() {
        FoodCategory cat = new FoodCategory("c2", "Produce", "leaf");
        FoodItem a = new FoodItem("a1", "Apple", cat, 1, "pcs", "2026-01-01", "2026-02-01");
        FoodItem b = new FoodItem("a2", "Banana", cat, 2, "pcs", "2026-01-01", "2026-02-02");
        inventoryService.addAllItems(List.of(a, b));
        assertEquals(2, inventoryService.getAllItems().size());
    }
}
