package model;

/**
 * One mutable row on the session grocery list (missing-ingredient checkout flow).
 * <p>
 * 会话购物清单上的一行可变数据（缺失食材 → 购物 → 结账流程）。
 */
public class GroceryItem {
    /** Row id for toggles and quantity updates. / 行 id，用于勾选与改数量。 */
    private final String id;
    /** Display name (usually missing ingredient name). / 显示名（通常为缺失食材名）。 */
    private final String name;
    /** Ingredient category for grouping in UI. / 食材分类，用于界面分组。 */
    private final FoodCategory category;
    /** Mutable quantity. / 可变数量。 */
    private int quantity;
    /** Unit price for subtotal = sum(qty * price) when collected. / 单价；已勾选时参与小计 Σ(数量×单价)。 */
    private final double price;
    /** Whether this row counts toward subtotal/tax/total. / 是否计入小计/税/总额。 */
    private boolean collected;

    /**
     * Creates a grocery row for the current session.
     * <p>
     * 为当前会话创建一条购物行。
     */
    public GroceryItem(
            String id,
            String name,
            FoodCategory category,
            int quantity,
            double price,
            boolean collected) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.collected = collected;
    }

    /** Returns row id. / 返回行 id。 */
    public String getId() {
        return id;
    }

    /** Returns display name. / 返回显示名。 */
    public String getName() {
        return name;
    }

    /** Returns ingredient category. / 返回食材分类。 */
    public FoodCategory getCategory() {
        return category;
    }

    /** Returns quantity. / 返回数量。 */
    public int getQuantity() {
        return quantity;
    }

    /** Returns unit price. / 返回单价。 */
    public double getPrice() {
        return price;
    }

    /** Returns whether the row is marked collected for checkout math. / 是否已勾选参与结账计算。 */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Updates quantity (service enforces non-negative floor).
     * <p>
     * 更新数量（服务层保证非负下限）。
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Updates collected flag for subtotal inclusion.
     * <p>
     * 更新「已采购」标记，用于是否计入小计。
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
