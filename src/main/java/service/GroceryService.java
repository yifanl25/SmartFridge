package service;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mutable in-session grocery list with PRD tax and subtotal rules.
 * <p>
 * 可变的会话内购物清单，实现 PRD 税率与小计规则。
 */
public class GroceryService implements IGroceryService {
    /** Fixed sales tax rate (8%). / 固定消费税率 8%。 */
    private static final double TAX_RATE = 0.08;
    /** Backing list of grocery rows. / 购物行 backing 列表。 */
    private final List<GroceryItem> groceryItems;

    /**
     * @param items initial rows (may be empty) / 初始行（可为空）
     */
    public GroceryService(List<GroceryItem> items) {
        this.groceryItems = new ArrayList<>(items);
    }

    /** {@inheritDoc} */
    @Override
    public List<GroceryItem> getItems() {
        return new ArrayList<>(groceryItems);
    }

    /** {@inheritDoc} */
    @Override
    public void addLine(GroceryItem item) {
        groceryItems.add(item);
    }

    /** {@inheritDoc} */
    @Override
    public GroceryItem toggleCollected(String itemId) {
        GroceryItem item = findById(itemId);
        item.setCollected(!item.isCollected());
        return item;
    }

    /** {@inheritDoc} */
    @Override
    public GroceryItem updateQuantity(String itemId, int delta) {
        GroceryItem item = findById(itemId);
        item.setQuantity(Math.max(0, item.getQuantity() + delta));
        return item;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteItem(String itemId) {
        groceryItems.removeIf(i -> i.getId().equals(itemId));
    }

    /** {@inheritDoc} */
    @Override
    public double calculateSubtotal() {
        return groceryItems.stream()
                .filter(GroceryItem::isCollected)
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    /** {@inheritDoc} */
    @Override
    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    /** {@inheritDoc} */
    @Override
    public double calculateTotal(double subtotal, double tax) {
        return subtotal + tax;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 本实现仅清空购物列表；与偏好/库存/推荐的联合清理由上层传入的 Runnable 处理。
     */
    @Override
    public void checkout() {
        clearGrocery();
    }

    /** {@inheritDoc} */
    @Override
    public void clearGrocery() {
        groceryItems.clear();
    }

    /**
     * Finds a row by id or throws.
     * <p>
     * 按 id 查找行，找不到则抛出异常。
     */
    private GroceryItem findById(String itemId) {
        Optional<GroceryItem> item = groceryItems.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
        return item.orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
    }
}
