package ui;

import controller.GroceryController;

/**
 * Stub Grocery page: row edits and checkout; totals use service PRD rules (collected rows only).
 * <p>
 * 购物页占位：行编辑与结账；金额规则由服务层 PRD（仅已勾选行）实现。
 */
public class GroceryPage {
    private final GroceryController groceryController;

    /**
     * @param groceryController grocery controller / 购物控制器
     */
    public GroceryPage(GroceryController groceryController) {
        this.groceryController = groceryController;
    }

    /** Placeholder render. / 占位渲染。 */
    public void render() {
        // 这里应该画出每一行购物，还能勾选、改数量、删行、看总价、结账 — 都问 groceryController。
        // Money rules 钱怎么算在 GroceryService，别在这一页自己乘 / Totals rules live in GroceryService.
        // INSERT YOUR CODE HERE
        System.out.println("Grocery Page");
    }

    /**
     * Checkout action; full session reset should be coordinated (e.g. {@link service.SessionReset}).
     * <p>
     * 结账动作；完整会话重置应由上层协调（如 {@link service.SessionReset}）。
     */
    public void submitCheckout() {
        groceryController.checkout();
    }
}
