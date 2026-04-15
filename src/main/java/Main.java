import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        manager.seedSampleData();

        System.out.println("--- Inventory ---");
        manager.getInventory().forEach(System.out::println);

        System.out.println("\n--- Sorted by expiry ---");
        List<FoodItem> byExpiry = manager.getItems("", "All", "expiry");
        byExpiry.forEach(System.out::println);

        System.out.println("\n--- Filter: Dairy ---");
        manager.getItems("", "Dairy", "expiry").forEach(System.out::println);

        System.out.println("\n--- Storage summary ---");
        for (Map.Entry<String, Long> entry : manager.getStorageSummary().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\nExpiring soon: " + manager.getExpiringSoonCount());
    }
}
