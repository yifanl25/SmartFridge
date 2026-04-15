package loader;

public class RecipeJsonFile {
    // File-system location of static recipes.json resource.
    private final String path;

    // Wrap configured recipe file path for loader wiring.
    public RecipeJsonFile(String path) {
        this.path = path;
    }

    // Return file path used by JsonRecipeLoader.
    public String getPath() {
        return path;
    }
}
