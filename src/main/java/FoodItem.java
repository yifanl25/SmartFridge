import java.time.LocalDate;

public class FoodItem {
    private final String id;
    private final String name;
    private final LocalDate expiryDate; // 过期时间
    private final String category;    // 分类

    public FoodItem(String id, String name, LocalDate expiryDate, String category) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    // Getter 方法
    public String getName() { return name; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("[%s] %s - 过期日期: %s", category, name, expiryDate);
    }
}