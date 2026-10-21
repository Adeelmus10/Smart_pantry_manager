package za.ac.richfield.smartpantry.model;

public class RecipeIngredient {
    private long id;
    private long recipeId;
    private String ingredientName;
    private double quantityNeeded;
    private String unit;

    public RecipeIngredient() {
    }

    public RecipeIngredient(String ingredientName, double quantityNeeded, String unit) {
        this.ingredientName = ingredientName;
        this.quantityNeeded = quantityNeeded;
        this.unit = unit;
    }

    public RecipeIngredient(long id, long recipeId, String ingredientName, double quantityNeeded, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantityNeeded = quantityNeeded;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
