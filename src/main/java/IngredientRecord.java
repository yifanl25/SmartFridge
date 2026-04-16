import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IngredientRecord(
        @JsonProperty("item_id") String itemId,
        String name,
        double amount,
        String unit
) {}
