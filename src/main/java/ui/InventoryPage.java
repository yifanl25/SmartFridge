package ui;

import controller.InventoryController;

/**
 * Stub Inventory screen: demonstrates controller wiring for card list / sort / filter (console placeholder).
 * <p>
 * 库存页占位：演示与控制器接线（控制台占位）。
 */
public class InventoryPage {
    private final InventoryController inventoryController;

    /**
     * @param inventoryController inventory MVC controller / 库存 MVC 控制器
     */
    public InventoryPage(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    /**
     * Placeholder render; real UI would show New/Urgent badges, category filter, sort controls.
     * <p>
     * 占位渲染；真实 UI 应展示新/临期角标、分类筛选、排序等。
     */
    public void render() {
        // 这里应该列出食材，还能筛选、排序 — 全部问 inventoryController 要数据，别自己 new 一份列表。
        // Show list + filter + sort using inventoryController only.
        // INSERT YOUR CODE HERE
        System.out.println("Inventory items: " + inventoryController.getVisibleItems().size());
    }
}
