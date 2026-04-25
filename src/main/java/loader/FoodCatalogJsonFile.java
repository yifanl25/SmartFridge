package loader;

/**
 * Simple holder for the food catalog JSON path (legacy wiring / documentation helper).
 */
public class FoodCatalogJsonFile {
    /** Filesystem or resource path string.  */
    private final String path;

    /**
     * @param path location passed to {@link JsonFoodCatalogLoader}
     */
    public FoodCatalogJsonFile(String path) {
        this.path = path;
    }

    /**
     * Returns configured path.
     */
    public String getPath() {
        return path;
    }
}
