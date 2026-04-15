package loader;

public class FoodCatalogJsonFile {
    // File-system location of static food_catalog.json resource.
    private final String path;

    // Wrap configured catalog path for loader wiring.
    public FoodCatalogJsonFile(String path) {
        this.path = path;
    }

    // Return file path used by JsonFoodCatalogLoader.
    public String getPath() {
        return path;
    }
}
