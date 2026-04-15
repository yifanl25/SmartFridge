package ui;

import controller.GroceryController;

public class GroceryPage {
    // Controller gateway for grocery row edits and checkout.
    private final GroceryController groceryController;

    // Wire grocery page to controller.
    public GroceryPage(GroceryController groceryController) {
        this.groceryController = groceryController;
    }

    // Render grocery list and summary area.
    // PRD notes:
    // - search/settings/save for later/header utility buttons are decorative/non-functional
    // - subtotal/tax/total are computed from collected rows only via service rules
    public void render() {
        System.out.println("Grocery Page");
    }

    // Checkout route action (end of loop).
    // App coordinator should also clear preference/inventory/recommendations for full PRD loop reset.
    public void submitCheckout() {
        groceryController.checkout();
    }
}
