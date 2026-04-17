package api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for the demo home dashboard.
 */
public class HomeDashboardResponse {

    private int inventoryCount;
    private int urgentInventoryCount;
    private int groceryCount;
    private int collectedGroceryCount;
    private String currentHealthGoal;
    private double subtotal;
    private double tax;
    private double total;
    private List<String> expiringSoon = new ArrayList<>();
    private List<String> topRecipeTitles = new ArrayList<>();

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public int getUrgentInventoryCount() {
        return urgentInventoryCount;
    }

    public void setUrgentInventoryCount(int urgentInventoryCount) {
        this.urgentInventoryCount = urgentInventoryCount;
    }

    public int getGroceryCount() {
        return groceryCount;
    }

    public void setGroceryCount(int groceryCount) {
        this.groceryCount = groceryCount;
    }

    public int getCollectedGroceryCount() {
        return collectedGroceryCount;
    }

    public void setCollectedGroceryCount(int collectedGroceryCount) {
        this.collectedGroceryCount = collectedGroceryCount;
    }

    public String getCurrentHealthGoal() {
        return currentHealthGoal;
    }

    public void setCurrentHealthGoal(String currentHealthGoal) {
        this.currentHealthGoal = currentHealthGoal;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<String> getExpiringSoon() {
        return new ArrayList<>(expiringSoon);
    }

    public void setExpiringSoon(List<String> expiringSoon) {
        this.expiringSoon = new ArrayList<>(expiringSoon);
    }

    public List<String> getTopRecipeTitles() {
        return new ArrayList<>(topRecipeTitles);
    }

    public void setTopRecipeTitles(List<String> topRecipeTitles) {
        this.topRecipeTitles = new ArrayList<>(topRecipeTitles);
    }
}
