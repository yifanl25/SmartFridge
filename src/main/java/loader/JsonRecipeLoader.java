package loader;

import model.Recipe;
import model.RecipeCategory;

import java.util.ArrayList;
import java.util.List;

public final class JsonRecipeLoader {
    // Utility loader; stateless and not instantiable.
    private JsonRecipeLoader() {
    }

    // Load static recipes data.
    // PRD contract:
    // - only static JSON source (recipes.json)
    // - no persistence and no dynamic authoring in MVP
    // - loaded recipe fields feed recommendation scoring and filtering
    // Current body is scaffold data and should be replaced by real JSON parsing.
    public static List<Recipe> loadFromFile(String path) {
        List<Recipe> recipes = new ArrayList<>();
        RecipeCategory cat = new RecipeCategory("quick", "Quick", "bolt");
        recipes.add(new Recipe(
                "r1", "Egg Bowl", cat, 0.8, 4.5, 15, 350, "Simple meal",
                List.of("Egg"), List.of("Rice")));
        return recipes;
    }
}
