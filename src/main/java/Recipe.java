import java.util.List;

/**
 * A recipe in the app.
 */
public class Recipe {

    private String id;
    private String name;
    private String category;
    private String imageUrl;
    private int cookTimeMin;
    private int servings;
    private double priceEstimateUsd;
    private double rating;

    /** Nutrition values per serving for goal-based filtering. */
    private int proteinG;
    private int calories;
    private int carbsG;
    private int fiberG;

    private List<String> tags;
    private List<String> requiredIngredients;

    /**
     * Constructs an empty Recipe.
     */
    public Recipe() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getCookTimeMin() { return cookTimeMin; }
    public void setCookTimeMin(int cookTimeMin) { this.cookTimeMin = cookTimeMin; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public double getPriceEstimateUsd() { return priceEstimateUsd; }
    public void setPriceEstimateUsd(double priceEstimateUsd) { this.priceEstimateUsd = priceEstimateUsd; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getProteinG() { return proteinG; }
    public void setProteinG(int proteinG) { this.proteinG = proteinG; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getCarbsG() { return carbsG; }
    public void setCarbsG(int carbsG) { this.carbsG = carbsG; }

    public int getFiberG() { return fiberG; }
    public void setFiberG(int fiberG) { this.fiberG = fiberG; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getRequiredIngredients() { return requiredIngredients; }
    public void setRequiredIngredients(List<String> requiredIngredients) { this.requiredIngredients = requiredIngredients; }
}