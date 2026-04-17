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
 * Loads recipe data from JSON.
 */
public final class JsonRecipeLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonRecipeLoader() {
    }

    /**
     * Loads recipes from a classpath resource or a file path.
     */
    public static List<Recipe> loadFromFile(String pathOrResource) throws IOException {
        InputStream in = JsonRecipeLoader.class.getResourceAsStream(
                pathOrResource.startsWith("/") ? pathOrResource : "/" + pathOrResource
        );

        if (in == null) {
            in = Files.newInputStream(Path.of(pathOrResource));
        }

        try (InputStream stream = in) {
            RecipeFile file = MAPPER.readValue(stream, RecipeFile.class);
            List<Recipe> result = new ArrayList<>();

            for (RecipeDto row : file.recipes) {
                RecipeCategory recipeCategory = new RecipeCategory(
                        row.recipeCategory.id,
                        row.recipeCategory.name,
                        row.recipeCategory.icon
                );

                List<Recipe.HealthTag> tags = parseHealthTags(row.healthTags);
                List<Recipe.Ingredient> requiredIngredients = mapIngredients(row.requiredIngredients);
                List<Recipe.Ingredient> optionalIngredients = mapIngredients(row.optionalIngredients);

                String description;
                if (row.description == null) {
                    description = "";
                } else {
                    description = row.description;
                }

                result.add(Recipe.loaded(
                        row.id,
                        row.title,
                        recipeCategory,
                        tags,
                        requiredIngredients,
                        optionalIngredients,
                        row.rating,
                        row.cookTime,
                        row.calories,
                        description
                ));
            }

            return result;
        }
    }

    /**
     * Loads recipes and wraps checked exceptions as IllegalStateException.
     */
    public static List<Recipe> loadFromFileSafe(String pathOrResource) {
        try {
            return loadFromFile(pathOrResource);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load recipes: " + pathOrResource, e);
        }
    }

    /**
     * Maps ingredient DTOs to recipe ingredients.
     */
    private static List<Recipe.Ingredient> mapIngredients(List<IngredientDto> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        List<Recipe.Ingredient> result = new ArrayList<>();

        for (IngredientDto dto : list) {
            if (dto == null || dto.name == null) {
                continue;
            }

            String quantityText;
            if (dto.quantityText == null) {
                quantityText = "";
            } else {
                quantityText = dto.quantityText;
            }

            result.add(new Recipe.Ingredient(dto.name, quantityText, dto.optional));
        }

        return result;
    }

    /**
     * Parses health tag strings into enum values.
     * Defaults to BALANCED if no valid tag is found.
     */
    private static List<Recipe.HealthTag> parseHealthTags(List<String> raw) {
        Set<Recipe.HealthTag> tags = EnumSet.noneOf(Recipe.HealthTag.class);

        if (raw != null) {
            for (String value : raw) {
                if (value == null || value.isEmpty()) {
                    continue;
                }

                try {
                    tags.add(Recipe.HealthTag.valueOf(value.trim()));
                } catch (IllegalArgumentException ignored) {
                    // Ignore unknown tag values in the JSON file.
                }
            }
        }

        if (tags.isEmpty()) {
            tags.add(Recipe.HealthTag.BALANCED);
        }

        return new ArrayList<>(tags);
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
