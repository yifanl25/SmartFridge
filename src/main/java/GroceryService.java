import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for the Grocery List page.
 *
 * 1. Generate grocery list from a selected recipe
 * 2. Filter by category (All / Dairy & Eggs / Bakery / ...)
 * 3. Search by ingredient name
 * 4. Mark item as collected
 * 5. Calculate Cart Summary
 */
public class GroceryService {

    private final GroceryRepository repository;
    private List<GroceryItem> groceryList = new ArrayList<>();

    // Constructor

    public GroceryService(GroceryRepository repository) {
        this.repository = repository;
    }


    // 1.Generate grocery list from a selected recipe

    /**
     * Given a recipe the user selected, find which ingredients are
     * missing or insufficient in the fridge and add them to the grocery list.
     *
     * @param recipeId e.g. "rec_003"
     * @return list of items that need to be purchased
     */
    public List<GroceryItem> generateGroceryListFromRecipe(String recipeId) {
        RecipesRecord recipe = repository.getRecipeById(recipeId);
        if (recipe == null) return new ArrayList<>();

        String recipeName = recipe.name();
        List<GroceryItem> needed = new ArrayList<>();

        for (IngredientRecord ingredient : recipe.ingredients()) {
            String itemId = ingredient.itemId();
            String name = ingredient.name();
            double amtNeeded = ingredient.amount();
            String unit = ingredient.unit();

            GroceryRepository.InventoryItem invItem = repository.getInventoryItemById(itemId);

            if (invItem == null) {
                // not in fridge at all → need to buy full amount
                needed.add(new GroceryItem(
                        itemId, name, "Unknown",
                        amtNeeded, unit, 0.0,
                        recipeId, recipeName
                ));
            } else {
                if (invItem.quantity < amtNeeded) {
                    // in fridge but not enough → buy the shortage
                    String category = repository.getCategoryByItemId(itemId);
                    double shortage = amtNeeded - invItem.quantity;
                    needed.add(new GroceryItem(
                            itemId, name, category,
                            shortage, unit, invItem.priceUsd,
                            recipeId, recipeName
                    ));
                }
                // else: enough in fridge → skip
            }
        }

        mergeIntoGroceryList(needed);
        return needed;
    }

    // avoid adding duplicate items
    private void mergeIntoGroceryList(List<GroceryItem> newItems) {
        for (GroceryItem newItem : newItems) {
            boolean exists = groceryList.stream()
                    .anyMatch(existing -> existing.getItemId().equals(newItem.getItemId()));
            if (!exists) {
                groceryList.add(newItem);
            }
        }
    }


    // 2.Filter by category

    /**
     * Returns the grocery list filtered by category.
     * Pass null or "All" to return everything.
     *
     * @param category e.g. "Dairy & Eggs", or null / "All"
     */
    public List<GroceryItem> filterByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return new ArrayList<>(groceryList);
        }
        return groceryList.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }


    // 3. Search by name

    /**
     * Returns items whose name contains the search keyword.
     * Case-insensitive.
     *
     * @param keyword e.g. "milk"
     */
    public List<GroceryItem> searchByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(groceryList);
        }
        return groceryList.stream()
                .filter(item -> item.getName().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }


    // 4. Mark item as collected

    /**
     * Toggles the collected state of an item.
     * Called when the user checks/unchecks an item.
     *
     * @param itemId e.g. "item_015"
     */
    public void toggleCollected(String itemId) {
        groceryList.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setCollected(!item.isCollected()));
    }


    // 5. Cart Summary


    /**
     * Builds a CartSummary from all currently collected items.
     * Includes subtotal, tax (10%), and total.
     */
    public CartSummary getCartSummary() {
        List<GroceryItem> collected = groceryList.stream()
                .filter(GroceryItem::isCollected)
                .collect(Collectors.toList());
        return new CartSummary(collected);
    }


    // Utility

    public void clearGroceryList() {
        groceryList.clear();
    }

    public int getTotalCount() {
        return groceryList.size();
    }

    public List<GroceryItem> getGroceryList() {
        return new ArrayList<>(groceryList);
    }
}
