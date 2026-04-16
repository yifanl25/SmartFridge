package controller;

import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;
import service.IInventoryService;
import service.InventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TDD：库存页控制器委托（对应 {@link InventoryController} → {@link IInventoryService}）。
 */
public class TestInventoryController {
    private InventoryController inventoryController;
    private IInventoryService inventoryService;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        inventoryService = new InventoryService(new FoodCatalog(List.of(new FoodCatalogEntry("Milk", 7, cat))));
        inventoryController = new InventoryController(inventoryService);
    }

    /**
     * 测试功能：获取当前可见库存列表（全部项）。
     * 验证点：初始为空。
     * <p>
     * 对应源码 / Maps to: {@link InventoryController#getVisibleItems()} → {@link InventoryService#getAllItems()}
     */
    @Test
    void testGetVisibleItemsDelegatesToInventoryService() {
        assertEquals(0, inventoryController.getVisibleItems().size());
    }

    /**
     * 测试功能：添加食材（目录解析名）。
     * 验证点：名称与目录一致。
     * <p>
     * 对应源码 / Maps to: {@link InventoryController#addItem(String)} → {@link InventoryService#addItem(String)}
     */
    @Test
    void testAddItemDelegatesToInventoryService() {
        assertEquals("Milk", inventoryController.addItem("Milk").getName());
    }

    /**
     * 测试功能：按食材分类筛选。
     * 验证点：筛选结果条数正确。
     * <p>
     * 对应源码 / Maps to: {@link InventoryController#filterByCategory(String)} → {@link InventoryService#filterByCategory(String)}
     */
    @Test
    void testFilterByCategoryDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.filterByCategory("Dairy").size());
    }

    /**
     * 测试功能：按过期日排序展示。
     * 验证点：委托服务并返回列表。
     * <p>
     * 对应源码 / Maps to: {@link InventoryController#sortByExpiry()} → {@link InventoryService#sortByExpiry()}
     */
    @Test
    void testSortByExpiryDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.sortByExpiry().size());
    }

    /**
     * 测试功能：按创建/入库时间排序。
     * 验证点：委托服务并返回列表。
     * <p>
     * 对应源码 / Maps to: {@link InventoryController#sortByCreatedTime()} → {@link InventoryService#sortByCreatedTime()}
     */
    @Test
    void testSortByCreatedTimeDelegatesToInventoryService() {
        inventoryController.addItem("Milk");
        assertEquals(1, inventoryController.sortByCreatedTime().size());
    }
}
