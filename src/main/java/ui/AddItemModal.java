package ui;

import controller.InventoryController;

/**
 * Stub Add Item modal: user must pick catalog suggestions; submit calls {@link InventoryController#addItem(String)}.
 * <p>
 * 「添加食材」弹窗占位：须选择目录建议；提交调用 {@link InventoryController#addItem(String)}。
 */
public class AddItemModal {
    private final InventoryController inventoryController;

    /**
     * @param inventoryController inventory controller / 库存控制器
     */
    public AddItemModal(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    /**
     * Placeholder open modal message.
     * <p>
     * 占位：打开弹窗提示。
     */
    public void render() {
        // 这里应该像控制台一样：先显示目录联想，再 submitFoodName / Like console: show catalog hints, then submitFoodName.
        // INSERT YOUR CODE HERE
        System.out.println("Add Item Modal");
    }

    /**
     * Submits the selected suggestion string (not arbitrary free text per PRD).
     * <p>
     * 提交所选建议字符串（PRD 禁止任意自由文本）。
     */
    public void submitFoodName(String foodName) {
        inventoryController.addItem(foodName);
    }
}
