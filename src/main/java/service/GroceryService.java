package service;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 这个类是真正管「购物清单状态」的地方。
 *
 * 大白话：
 * - grocery 里现在有哪些行
 * - 勾选没勾选
 * - 数量改了多少
 * - 小计、税、总价怎么算
 * - 结账后怎么清空
 *
 * 都是在这里处理的。
 */
public class GroceryService implements IGroceryService {
    // 这里先按你 PRD 里写的固定税率 8% 算。
    private static final double TAX_RATE = 0.08;

    // 这一坨就是当前会话里的购物清单数据。
    // 注意它是可变的，所以增删改都会动到这份 list。
    private final List<GroceryItem> groceryItems;

    /**
     * 构造函数。
     *
     * 传进来的 items 先拷贝一份，
     * 这样外面那份 list 后面乱改，不会直接污染这里。
     */
    public GroceryService(List<GroceryItem> items) {
        this.groceryItems = new ArrayList<>(items);
    }

    /**
     * 返回当前购物清单。
     *
     * 这里返回的是拷贝，不是内部原 list，
     * 免得外部直接拿到引用后乱改。
     */
    @Override
    public List<GroceryItem> getItems() {
        return new ArrayList<>(groceryItems);
    }

    /**
     * 追加一条购物行。
     *
     * 常见场景：
     * - 前端手动加一项
     * - 从 recipe 缺失食材自动导入一项
     */
    @Override
    public void addLine(GroceryItem item) {
        groceryItems.add(item);
    }

    /**
     * 按分类筛选购物项。
     *
     * 规则：
     * - category 为空 / 空白 / All -> 直接返回全部
     * - 否则按 category name 精确匹配
     */
    @Override
    // ===== teammate note =====
    // 这是从组员逻辑里抽进来的分类筛选。
    // 如果后面分类展示名、大小写、id/name 映射变了，就在这里统一处理。
    // insert your code here: refine category matching rules
    public List<GroceryItem> filterByCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank() || categoryName.equalsIgnoreCase("All")) {
            return new ArrayList<>(groceryItems);
        }
        String needle = categoryName.trim().toLowerCase(Locale.ROOT);
        return groceryItems.stream()
                .filter(i -> i.getCategory().getName().toLowerCase(Locale.ROOT).equals(needle))
                .collect(Collectors.toList());
    }

    /**
     * 按名字搜索购物项。
     *
     * 这里是“包含匹配”，不是必须完全等于。
     * 比如搜 mil，可以匹配到 milk。
     */
    @Override
    // ===== teammate note =====
    // 这是 grocery 的名字搜索。
    // 后面如果要支持模糊匹配、alias、拼写纠正，直接从这里补。
    // insert your code here: upgrade search behavior
    public List<GroceryItem> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(groceryItems);
        }
        String needle = keyword.trim().toLowerCase(Locale.ROOT);
        return groceryItems.stream()
                .filter(i -> i.getName().toLowerCase(Locale.ROOT).contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * 切换某一项是否已买到。
     *
     * 为什么这个状态重要？
     * 因为你这里的小计不是算全部项，
     * 而是只算已经 collected 的项。
     */
    @Override
    public GroceryItem toggleCollected(String itemId) {
        GroceryItem item = findById(itemId);
        item.setCollected(!item.isCollected());
        return item;
    }

    /**
     * 按“增量”改数量。
     *
     * 比如当前是 2，delta = 3，就变成 5。
     * 如果 delta 是负数，也可以往下减。
     *
     * 这里做了下限保护：最少只能到 0，不会变负数。
     */
    @Override
    public GroceryItem updateQuantity(String itemId, int delta) {
        GroceryItem item = findById(itemId);
        item.setQuantity(Math.max(0, item.getQuantity() + delta));
        return item;
    }

    /**
     * 删除一条购物项。
     */
    @Override
    public void deleteItem(String itemId) {
        groceryItems.removeIf(i -> i.getId().equals(itemId));
    }

    /**
     * 算小计。
     *
     * 这里的规则是：
     * - 只统计已经 collected 的项
     * - 每项金额 = 单价 * 数量
     */
    @Override
    public double calculateSubtotal() {
        return groceryItems.stream()
                .filter(GroceryItem::isCollected)
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    /**
     * 税额 = 小计 * 固定税率。
     */
    @Override
    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    /**
     * 总额 = 小计 + 税。
     */
    @Override
    public double calculateTotal(double subtotal, double tax) {
        return subtotal + tax;
    }

    /**
     * 购物模块自己的 checkout。
     *
     * 这里目前只负责清空 grocery。
     * 如果你还想在结账时顺便清库存、清偏好、重置推荐，
     * 那种“整条流程一起清”的动作应该交给上层协调。
     */
    @Override
    public void checkout() {
        clearGrocery();
    }

    /**
     * 直接清空整个购物清单。
     */
    @Override
    public void clearGrocery() {
        groceryItems.clear();
    }

    /**
     * 按 id 找购物项。
     *
     * 找不到就直接抛错，
     * 这样上层就知道传进来的 itemId 有问题。
     */
    private GroceryItem findById(String itemId) {
        Optional<GroceryItem> item = groceryItems.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
        return item.orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
    }
}
