package controller;

import model.GroceryItem;
import service.IGroceryService;

import java.util.List;

/**
 * MVC controller for the Grocery list, totals, and checkout; delegates to {@link IGroceryService}.
 * Optional {@link Runnable} runs after checkout for full session reset (PRD).
 * <p>
 * 购物清单、汇总与结账控制器；委托 {@link IGroceryService}。可选 {@link Runnable} 在结账后执行完整会话重置（PRD）。
 * <p>删一行、结账怎么清空：看 {@link api.web.GroceryApiController} 上面的注释 / Delete + checkout notes live in GroceryApiController.</p>
 */
public class GroceryController {
    /** Grocery state and pricing. / 购物状态与计价。 */
    private final IGroceryService groceryService;
    /**
     * Invoked on {@link #checkout()}; may clear only grocery or full session via {@link service.SessionReset}.
     * <p>
     * 在 {@link #checkout()} 时调用；可仅清购物或经 {@link service.SessionReset} 清全会话。
     */
    private final Runnable onCheckoutLoopEnd;

    /**
     * Default: checkout runs {@link IGroceryService#checkout()} only.
     * <p>
     * 默认：结账仅调用 {@link IGroceryService#checkout()}。
     */
    public GroceryController(IGroceryService groceryService) {
        this(groceryService, groceryService::checkout);
    }

    /**
     * @param groceryService      grocery service / 购物服务
     * @param onCheckoutLoopEnd   extra hook after checkout (e.g. session reset) / 结账后的额外钩子（如会话重置）
     */
    public GroceryController(IGroceryService groceryService, Runnable onCheckoutLoopEnd) {
        this.groceryService = groceryService;
        this.onCheckoutLoopEnd = onCheckoutLoopEnd;
    }

    /**
     * Returns current grocery rows from the service.
     * <p>
     * 从服务返回当前购物行。
     */
    public List<GroceryItem> getItems() {
        return groceryService.getItems();
    }

    /**
     * Appends a row (e.g. from missing-ingredient flow).
     * <p>
     * 追加一行（例如来自缺失食材流程）。
     */
    public void addLine(GroceryItem item) {
        groceryService.addLine(item);
    }

    /** {@inheritDoc} */
    public GroceryItem toggleCollected(String itemId) {
        return groceryService.toggleCollected(itemId);
    }

    /** {@inheritDoc} */
    public GroceryItem updateQuantity(String itemId, int delta) {
        return groceryService.updateQuantity(itemId, delta);
    }

    /** {@inheritDoc} */
    public void deleteItem(String itemId) {
        groceryService.deleteItem(itemId);
    }

    /** {@inheritDoc} */
    public double calculateSubtotal() {
        return groceryService.calculateSubtotal();
    }

    /** {@inheritDoc} */
    public double calculateTax(double subtotal) {
        return groceryService.calculateTax(subtotal);
    }

    /** {@inheritDoc} */
    public double calculateTotal(double subtotal, double tax) {
        return groceryService.calculateTotal(subtotal, tax);
    }

    /**
     * Runs checkout hook (typically clears grocery and may reset whole session).
     * <p>
     * 执行结账钩子（通常清空购物并可能重置整会话）。
     */
    public void checkout() {
        onCheckoutLoopEnd.run();
    }
}
