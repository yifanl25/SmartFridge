package loader;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Recipe;
import model.RecipeCategory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Loads {@code recipes.json} into {@link Recipe} templates using {@link Recipe#loaded}.
 * <p>
 * The loaded recipes contain only static data from JSON — no runtime scores,
 * availability lists, or match results are computed here. Those are added later
 * by the recommendation service via {@link Recipe#withComputed}.
 * </p>
 */
public final class JsonRecipeLoader {
    /**
     * Shared Jackson mapper instance for JSON parsing.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Private constructor — this is a utility class and should not be instantiated.
     */
    private JsonRecipeLoader() {
    }

    /**
     * Loads and parses recipes from the given file path or classpath resource.
     * <p>
     * First attempts to load the file as a classpath resource; if not found,
     * falls back to reading it directly from the file system path.
     * Unknown health tag strings are silently skipped. If no valid tags are found,
     * the recipe defaults to {@link Recipe.HealthTag#BALANCED}.
     * </p>
     *
     * @param pathOrResource classpath resource path (e.g. "recipes.json") or file system path
     * @return list of recipe templates with no computed fields
     * @throws IOException if the file cannot be read or parsed
     */
    public static List<Recipe> loadFromFile(String pathOrResource) throws IOException {
        InputStream in = JsonRecipeLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource);
        if (in == null) {
            in = Files.newInputStream(Path.of(pathOrResource));
        }
        try (InputStream stream = in) {
            RecipeFile file = MAPPER.readValue(stream, RecipeFile.class);
            List<Recipe> out = new ArrayList<>();
            for (RecipeDto row : file.recipes) {
                RecipeCategory rc = new RecipeCategory(
                        row.recipeCategory.id,
                        row.recipeCategory.name,
                        row.recipeCategory.icon);
                List<Recipe.HealthTag> tags = parseHealthTags(row.healthTags);
                List<Recipe.Ingredient> req = mapIngredients(row.requiredIngredients);
                List<Recipe.Ingredient> opt = mapIngredients(row.optionalIngredients);
                String desc = row.description == null ? "" : row.description;
                out.add(Recipe.loaded(
                        row.id,
                        row.title,
                        rc,
                        tags,
                        req,
                        opt,
                        row.rating,
                        row.cookTime,
                        row.calories,
                        desc));
            }
            return out;
        }
    }

    /**
     * Same as {@link #loadFromFile(String)} but wraps any {@link IOException}
     * in an unchecked {@link IllegalStateException}.
     * <p>
     * Use this when the caller cannot handle checked exceptions
     * (e.g. during application startup or Spring bean initialization).
     * </p>
     *
     * @param pathOrResource classpath resource path or file system path
     * @return list of recipe templates with no computed fields
     * @throws IllegalStateException if the file cannot be read or parsed
     */
    public static List<Recipe> loadFromFileSafe(String pathOrResource) {
        try {
            return loadFromFile(pathOrResource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load recipes: " + pathOrResource, e);
        }
    }

    /**
     * Converts a list of {@link IngredientDto} objects into {@link Recipe.Ingredient} instances.
     * <p>
     * Entries with a {@code null} name are silently skipped.
     * A {@code null} {@code quantityText} is replaced with an empty string.
     * </p>
     *
     * @param list the raw DTO list from JSON, may be {@code null}
     * @return list of {@link Recipe.Ingredient}, or an empty list if input is null
     */
    private static List<Recipe.Ingredient> mapIngredients(List<IngredientDto> list) {
        if (list == null) {
            return List.of();
        }
        List<Recipe.Ingredient> out = new ArrayList<>();
        for (IngredientDto d : list) {
            if (d == null || d.name == null) {
                continue;
            }
            String q = d.quantityText == null ? "" : d.quantityText;
            out.add(new Recipe.Ingredient(d.name, q, d.optional));
        }
        return out;
    }

    /**
     * Parses a list of raw health tag strings into a list of {@link Recipe.HealthTag} enum values.
     * <p>
     * Unrecognized tag strings are silently ignored.
     * If no valid tags are found, defaults to {@link Recipe.HealthTag#BALANCED}.
     * </p>
     *
     * @param raw list of raw tag strings from JSON, may be {@code null}
     * @return list of valid {@link Recipe.HealthTag} values, never empty
     */
    private static List<Recipe.HealthTag> parseHealthTags(List<String> raw) {
        Set<Recipe.HealthTag> set = EnumSet.noneOf(Recipe.HealthTag.class);
        if (raw != null) {
            for (String s : raw) {
                if (s == null || s.isEmpty()) {
                    continue;
                }
                try {
                    set.add(Recipe.HealthTag.valueOf(s.trim()));
                } catch (IllegalArgumentException ignored) {
                    // ignore unknown tags in JSON
                }
            }
        }
        if (set.isEmpty()) {
            set.add(Recipe.HealthTag.BALANCED);
        }
        return new ArrayList<>(set);
    }

    /**
     * Top-level JSON structure mapping the {@code recipes} array.
     * Fields are populated by Jackson via reflection.
     */
    @SuppressWarnings("unused")
    private static class RecipeFile {
        public List<RecipeDto> recipes;
    }

    /**
     * JSON representation of a single recipe entry.
     * Fields are populated by Jackson via reflection.
     */
    @SuppressWarnings("unused")
    private static class RecipeDto {
        public String id;
        public String title;
        public CategoryDto recipeCategory;
        public List<String> healthTags;
        public List<IngredientDto> requiredIngredients;
        public List<IngredientDto> optionalIngredients;
        public double rating;
        public int cookTime;
        public int calories;
        public String description;
    }

    /**
     * JSON representation of a recipe category.
     * Fields are populated by Jackson via reflection.
     */
    @SuppressWarnings("unused")
    private static class CategoryDto {
        public String id;
        public String name;
        public String icon;
    }

    /**
     * JSON representation of a single ingredient line on a recipe.
     * Fields are populated by Jackson via reflection.
     */
    @SuppressWarnings("unused")
    private static class IngredientDto {
        public String name;
        public String quantityText;
        public boolean optional;
    }
}
