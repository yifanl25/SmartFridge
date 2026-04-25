package loader;

/**
 * Simple holder for the recipes JSON path (legacy wiring / documentation helper).
 * <p>
 */
public class RecipeJsonFile {
    /** Filesystem or resource path string.  */
    private final String path;

    /**
     * @param path location passed to {@link JsonRecipeLoader}
     */
    public RecipeJsonFile(String path) {
        this.path = path;
    }

    /**
     * Returns configured path.
     */
    public String getPath() {
        return path;
    }
}
