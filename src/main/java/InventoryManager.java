import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    // 使用 final 确保引用不被篡改
    private final List<FoodItem> inventory = new ArrayList<>();

    // 逻辑：添加食物（对应“自行增加东西，目前输入字符增加”）
    public void addFood(String name, String expiryDateStr, String category) {
        FoodItem newItem = new FoodItem(
                String.valueOf(System.currentTimeMillis()),
                name,
                LocalDate.parse(expiryDateStr),
                category
        );
        inventory.add(newItem);
    }

    /**
     * 静态方法：实现你备注里的“按照新旧顺序和预期过期时间排序”
     * 逻辑：从紧急（快过期）到不紧急
     */
    public static List<FoodItem> sortItemsByUrgency(List<FoodItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(FoodItem::getExpiryDate))
                .collect(Collectors.toList());
    }

    /**
     * 逻辑：根据分类过滤（对应“点这个可以看到分类”）
     */
    public List<FoodItem> filterByCategory(String category) {
        return inventory.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<FoodItem> getInventory() {
        return inventory;
    }
}