package controller;

import model.FoodItem;
import service.IInventoryService;

import java.util.List;

/**
 * MVC controller for the Inventory screen and Add Item flow: delegates all rules to {@link IInventoryService}.
 * <p>
 * 库存页与「添加食材」流程的控制器：业务规则全部委托给 {@link IInventoryService}。
 * <p>手机要调筛选/排序：去改 {@link api.web.InventoryApiController}，这里<strong>不用</strong>重复写规则。</p>
 */
public class InventoryController {
    /** Backing service for session inventory. / 会话库存的后端服务。 */
    private final IInventoryService inventoryService;

    /**
     * @param inventoryService injected inventory implementation / 注入的库存实现
     */
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Returns all items for list rendering.
     * <p>
     * 返回全部库存项供列表渲染。
     */
    public List<FoodItem> getVisibleItems() {
        return inventoryService.getAllItems();
    }

    /**
     * Adds one item after user picked a catalog suggestion (PRD: no arbitrary free-text add).
     * <p>
     * 用户选择目录建议后添加一条（PRD：不允许任意自由文本添加）。
     */
    public FoodItem addItem(String foodName) {
        return inventoryService.addItem(foodName);
    }

    /**
     * Filters by ingredient {@link model.FoodCategory} display name (not recipe category).
     * <p>
     * 按食材 {@link model.FoodCategory} 显示名筛选（非菜谱分类）。
     */
    public List<FoodItem> filterByCategory(String categoryName) {
        return inventoryService.filterByCategory(categoryName);
    }

    /**
     * Sorts by expiry date (earlier first).
     * <p>
     * 按过期日排序（越早越靠前）。
     */
    public List<FoodItem> sortByExpiry() {
        return inventoryService.sortByExpiry();
    }

    /**
     * Sorts by created date (newest first in service).
     * <p>
     * 按创建日排序（服务实现中为新到旧）。
     */
    public List<FoodItem> sortByCreatedTime() {
        return inventoryService.sortByCreatedTime();
    }
}
