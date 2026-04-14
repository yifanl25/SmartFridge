import java.util.List;

public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // 模拟用户输入增加东西
        manager.addFood("牛奶", "2026-04-20", "乳制品");
        manager.addFood("鸡蛋", "2026-04-15", "蛋类");
        manager.addFood("苹果", "2026-05-01", "水果");

        System.out.println("--- 原始库存 ---");
        manager.getInventory().forEach(System.out::println);

        // 测试：从紧急到不紧急排序
        System.out.println("\n--- 按照过期时间排序 (紧急优先) ---");
        List<FoodItem> sortedList = InventoryManager.sortItemsByUrgency(manager.getInventory());
        sortedList.forEach(System.out::println);

        // 测试：分类查看
        System.out.println("\n--- 查看分类: 水果 ---");
        manager.filterByCategory("水果").forEach(System.out::println);
    }
}