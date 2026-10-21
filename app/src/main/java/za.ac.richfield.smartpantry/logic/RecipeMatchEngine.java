package za.ac.richfield.smartpantry.logic;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantry.model.Recipe;
import za.ac.richfield.smartpantry.model.RecipeIngredient;

public class RecipeMatchEngine {

    public static boolean evaluate(Recipe recipe, PantrySnapshot snapshot) {
        if (recipe == null) {
            return false;
        }
        recipe.getMissingIngredients().clear();

        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return true;
        }

        if (snapshot == null) {
            for (RecipeIngredient req : recipe.getIngredients()) {
                recipe.getMissingIngredients().add(req.getIngredientName());
            }
            return false;
        }

        for (RecipeIngredient req : recipe.getIngredients()) {
            boolean satisfied = snapshot.isRequirementSatisfied(req.getIngredientName(), req.getQuantityNeeded(), req.getUnit());
            if (!satisfied) {
                recipe.getMissingIngredients().add(req.getIngredientName());
            }
        }

        return recipe.getMissingIngredients().isEmpty();
    }

    public static List<Recipe> suggest(List<Recipe> catalogue, PantrySnapshot snapshot) {
        List<Recipe> suggestions = new ArrayList<>();
        if (catalogue == null) {
            return suggestions;
        }

        for (Recipe recipe : catalogue) {
            if (evaluate(recipe, snapshot)) {
                suggestions.add(recipe);
            }
        }

        return suggestions;
    }
}
