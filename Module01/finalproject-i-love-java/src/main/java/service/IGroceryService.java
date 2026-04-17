package service;

import model.GroceryItem;

import java.util.List;

/**
 * 这是 grocery 模块的 service 接口。
 *
 * 大白话：
 * 只要是“购物清单应该会做的事”，
 * 都先在这里列出来，
 * 然后具体由 GroceryService 去实现。
 *
 * 这样 controller / api 只依赖接口，
 * 分层会更清楚。
 */
public interface IGroceryService {

    /**
     * 取当前购物清单全部行。
     */
    List<GroceryItem> getItems();

    /**
     * 加一条购物项。
     */
    void addLine(GroceryItem item);

    /**
     * 按分类筛选。
     */
        // teammate note: 这个接口是给 grocery 分类筛选用的。
    // insert your code here: keep interface in sync if service rule changes
List<GroceryItem> filterByCategory(String categoryName);

    /**
     * 按名字关键字搜索。
     */
        // teammate note: 这个接口是给 grocery 名字搜索用的。
    // insert your code here: keep interface in sync if search rule changes
List<GroceryItem> searchByName(String keyword);

    /**
     * 切换某行是否已买。
     *
     * 这里会返回改完后的那一行，
     * 方便上层直接拿去回给前端。
     */
    GroceryItem toggleCollected(String itemId);

    /**
     * 按增量修改数量。
     */
    GroceryItem updateQuantity(String itemId, int delta);

    /**
     * 删除一条购物项。
     */
    void deleteItem(String itemId);

    /**
     * 计算小计。
     *
     * 规则：只统计 collected 的项，
     * 每项金额 = 数量 * 单价。
     */
    double calculateSubtotal();

    /**
     * 计算税额。
     */
    double calculateTax(double subtotal);

    /**
     * 计算总额。
     */
    double calculateTotal(double subtotal, double tax);

    /**
     * checkout 时，购物模块自己要做的收尾动作。
     */
    void checkout();

    /**
     * 直接清空购物清单。
     */
    void clearGrocery();
}
