package loader;

/**
 * Simple holder for the recipes JSON path (legacy wiring / documentation helper).
 * <p>
 * 持有菜谱 JSON 路径的简单包装（遗留接线/文档辅助）。
 */
public class RecipeJsonFile {
    /** Filesystem or resource path string. / 文件系统或资源路径字符串。 */
    private final String path;

    /**
     * @param path location passed to {@link JsonRecipeLoader} / 传给 {@link JsonRecipeLoader} 的位置
     */
    public RecipeJsonFile(String path) {
        this.path = path;
    }

    /**
     * Returns configured path.
     * <p>
     * 返回配置的路径。
     */
    public String getPath() {
        return path;
    }
}
