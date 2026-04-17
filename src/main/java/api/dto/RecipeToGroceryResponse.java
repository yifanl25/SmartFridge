package api.dto;

import model.GroceryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是“从某道菜加入 grocery list”后的响应。
 *
 * 大白话：
 * 前端点了“把缺的食材加入购物清单”以后，
 * 不只是想知道成没成功，
 * 还想知道：
 * - 是哪道菜触发的
 * - 新增了几条
 * - 合并了几条
 * - 新增的是哪些行
 * - 合并的是哪些名字
 *
 * 所以这里专门做一个 response，别直接丢个布尔值完事。
 *
 * <p>Teammate note: 这个 DTO 是为了告诉前端“这次把 recipe 加进 grocery 后到底发生了什么”。
 * 后面如果还要加 skippedItems、errorItems、unitSummary 之类的字段，就继续在这里扩。
 * insert your code here: add extra response fields only if UI really needs them</p>
 */
public class RecipeToGroceryResponse {
    private String recipeId;
    private String recipeTitle;
    private int addedCount;
    private int mergedCount;
    private List<GroceryItem> addedItems = new ArrayList<>();
    private List<String> mergedItemNames = new ArrayList<>();

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(int addedCount) {
        this.addedCount = addedCount;
    }

    public int getMergedCount() {
        return mergedCount;
    }

    public void setMergedCount(int mergedCount) {
        this.mergedCount = mergedCount;
    }

    public List<GroceryItem> getAddedItems() {
        return new ArrayList<>(addedItems);
    }

    public void setAddedItems(List<GroceryItem> addedItems) {
        this.addedItems = new ArrayList<>(addedItems);
    }

    public List<String> getMergedItemNames() {
        return new ArrayList<>(mergedItemNames);
    }

    public void setMergedItemNames(List<String> mergedItemNames) {
        this.mergedItemNames = new ArrayList<>(mergedItemNames);
    }
}
