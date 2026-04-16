import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecipesRecord(
        String id,
        String name,
        String category,
        int servings,
        int calories,
        double rating,
        @JsonProperty("cook_time_min") int cookTimeMin,
        @JsonProperty("price_estimate_usd") double priceEstimateUsd,
        @JsonProperty("protein_g") int proteinG,
        @JsonProperty("carbs_g") int carbsG,
        @JsonProperty("fiber_g") int fiberG,
        List<String> tags,
        List<String> instructions,
        List<IngredientRecord> ingredients
) {}
