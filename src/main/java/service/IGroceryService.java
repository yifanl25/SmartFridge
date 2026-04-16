package service;

import model.GroceryItem;

import java.util.List;

/**
 * Session grocery list: rows, edits, pricing summary, checkout and clear.
 * <p>
 * 会话购物清单：行数据、编辑、计价汇总、结账与清空。
 */
public interface IGroceryService {

    /**
     * Returns current grocery rows (defensive copy semantics up to implementation).
     * <p>
     * 返回当前购物行（具体是否防御性拷贝由实现决定）。
     */
    List<GroceryItem> getItems();

    /**
     * Appends one row (e.g. built from missing recipe ingredients).
     * <p>
     * 追加一行（例如由菜谱缺失食材生成）。
     */
    void addLine(GroceryItem item);

    /**
     * Toggles collected flag; only collected rows count in subtotal.
     * <p>
     * 切换「已采购」标记；仅已勾选行计入小计。
     */
    GroceryItem toggleCollected(String itemId);

    /**
     * Adjusts quantity by delta (floor at zero in service).
     * <p>
     * 按增量调整数量（服务层将数量下限钳制为 0）。
     */
    GroceryItem updateQuantity(String itemId, int delta);

    /**
     * Removes one row by id.
     * <p>
     * 按 id 删除一行。
     */
    void deleteItem(String itemId);

    /**
     * Subtotal = sum(quantity * price) for collected rows only.
     * <p>
     * 小计 = 仅对已勾选行求和（数量×单价）。
     */
    double calculateSubtotal();

    /**
     * Tax = subtotal * 0.08 (PRD fixed rate).
     * <p>
     * 税额 = 小计 × 0.08（PRD 固定税率）。
     */
    double calculateTax(double subtotal);

    /**
     * Total = subtotal + tax.
     * <p>
     * 总额 = 小计 + 税。
     */
    double calculateTotal(double subtotal, double tax);

    /**
     * Checkout hook for grocery module (typically clears grocery list).
     * <p>
     * 购物模块结账钩子（通常清空购物列表）。
     */
    void checkout();

    /**
     * Clears all grocery rows (session reset coordinator may call this).
     * <p>
     * 清空全部购物行（会话重置协调器可调用）。
     */
    void clearGrocery();
}
