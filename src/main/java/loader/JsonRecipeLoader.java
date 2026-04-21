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
 * Loads {@code recipes.json} into {@link Recipe#loaded} templates (no runtime scores yet).
 * <p>
 */
public final class JsonRecipeLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonRecipeLoader() {
    }

    /**
     * Parses recipes array; unknown health tag strings are skipped; empty tags default to {@link Recipe.HealthTag#BALANCED}.
     * <p>
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
     * Same as {@link #loadFromFile(String)} with unchecked exception for callers.
     * <p>
     */
    public static List<Recipe> loadFromFileSafe(String pathOrResource) {
        try {
            return loadFromFile(pathOrResource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load recipes: " + pathOrResource, e);
        }
    }

    /**
     * Maps DTO list to {@link Recipe.Ingredient} list; skips null names.
     * <p>
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
     * Parses string tags into enum set; defaults to {@link Recipe.HealthTag#BALANCED} if none valid.
     * <p>
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

    @SuppressWarnings("unused")
    private static class RecipeFile {
        public List<RecipeDto> recipes;
    }

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

    @SuppressWarnings("unused")
    private static class CategoryDto {
        public String id;
        public String name;
        public String icon;
    }

    @SuppressWarnings("unused")
    private static class IngredientDto {
        public String name;
        public String quantityText;
        public boolean optional;
    }
}
