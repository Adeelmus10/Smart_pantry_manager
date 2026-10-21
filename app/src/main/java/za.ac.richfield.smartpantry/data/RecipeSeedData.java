package za.ac.richfield.smartpantry.data;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantry.model.Recipe;
import za.ac.richfield.smartpantry.model.RecipeIngredient;

public class RecipeSeedData {

    public static List<Recipe> buildAll() {
        List<Recipe> recipes = new ArrayList<>();

        // 1. Tomato Omelette (6 ingredients) - MATCHES Starter Pantry (needs Butter)
        Recipe r1 = new Recipe(1, "Tomato Omelette", "Breakfast", "Whisk eggs with salt and pepper. Melt butter in pan, add diced tomatoes, pour eggs, top with cheese and fold when set.", 10, 10);
        r1.addIngredient(new RecipeIngredient("Eggs", 3, "units"));
        r1.addIngredient(new RecipeIngredient("Tomatoes", 2, "units"));
        r1.addIngredient(new RecipeIngredient("Butter", 15, "g"));
        r1.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r1.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        r1.addIngredient(new RecipeIngredient("Cheddar Cheese", 50, "g"));
        recipes.add(r1);

        // 2. Two-Egg Pancakes (6 ingredients) - MATCHES Starter Pantry (needs Butter)
        Recipe r2 = new Recipe(2, "Two-Egg Pancakes", "Breakfast", "Whisk eggs, milk, sugar, and melted butter. Mix in flour and salt. Cook ladles of batter on a greased skillet until golden.", 10, 15);
        r2.addIngredient(new RecipeIngredient("Eggs", 2, "units"));
        r2.addIngredient(new RecipeIngredient("Flour", 150, "g"));
        r2.addIngredient(new RecipeIngredient("Milk", 200, "ml"));
        r2.addIngredient(new RecipeIngredient("Butter", 20, "g"));
        r2.addIngredient(new RecipeIngredient("Sugar", 30, "g"));
        r2.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r2);

        // 3. Butter Spaghetti (3 ingredients) - MATCHES Starter Pantry (needs Butter)
        Recipe r3 = new Recipe(3, "Butter Spaghetti", "Dinner", "Boil pasta in salted water until al dente. Drain, leaving a spoon of pasta water, then toss with butter and salt.", 5, 12);
        r3.addIngredient(new RecipeIngredient("Pasta", 200, "g"));
        r3.addIngredient(new RecipeIngredient("Butter", 30, "g"));
        r3.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r3);

        // 4. Garlic Butter Rice (4 ingredients) - MATCHES Starter Pantry (needs Butter)
        Recipe r4 = new Recipe(4, "Garlic Butter Rice", "Side", "Sauté minced garlic in butter until fragrant. Add cooked rice and salt, toss thoroughly over medium heat.", 5, 10);
        r4.addIngredient(new RecipeIngredient("Rice", 200, "g"));
        r4.addIngredient(new RecipeIngredient("Butter", 20, "g"));
        r4.addIngredient(new RecipeIngredient("Garlic", 2, "cloves"));
        r4.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r4);

        // 5. Classic Pasta (5 ingredients) - MATCHES Starter Pantry (NO Butter)
        Recipe r5 = new Recipe(5, "Classic Pasta", "Dinner", "Boil pasta. Sauté garlic and chopped tomatoes in olive oil with salt. Toss pasta with the fresh tomato sauce.", 10, 15);
        r5.addIngredient(new RecipeIngredient("Pasta", 200, "g"));
        r5.addIngredient(new RecipeIngredient("Tomatoes", 3, "units"));
        r5.addIngredient(new RecipeIngredient("Garlic", 2, "cloves"));
        r5.addIngredient(new RecipeIngredient("Olive Oil", 20, "ml"));
        r5.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r5);

        // 6. Simple Salad (5 ingredients) - MATCHES Starter Pantry (NO Butter)
        Recipe r6 = new Recipe(6, "Simple Salad", "Salad", "Chop tomatoes and onions into a bowl. Drizzle with olive oil, sprinkle salt and pepper, and toss gently.", 5, 0);
        r6.addIngredient(new RecipeIngredient("Tomatoes", 3, "units"));
        r6.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r6.addIngredient(new RecipeIngredient("Olive Oil", 15, "ml"));
        r6.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r6.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r6);

        // 7. Scrambled Eggs (3 ingredients)
        Recipe r7 = new Recipe(7, "Scrambled Eggs", "Breakfast", "Whisk eggs with a pinch of salt. Melt butter over low heat, add eggs, chives, and gently stir until soft curds form.", 3, 5);
        r7.addIngredient(new RecipeIngredient("Eggs", 3, "units"));
        r7.addIngredient(new RecipeIngredient("Butter", 15, "g"));
        r7.addIngredient(new RecipeIngredient("Chives", 10, "g"));
        recipes.add(r7);

        // 8. Chicken Stir Fry (6 ingredients)
        Recipe r8 = new Recipe(8, "Chicken Stir Fry", "Dinner", "Heat oil in wok. Sauté sliced chicken until cooked. Add minced garlic, onions, carrots, and soy sauce. Stir fry 5 mins.", 15, 15);
        r8.addIngredient(new RecipeIngredient("Chicken Breast", 300, "g"));
        r8.addIngredient(new RecipeIngredient("Soy Sauce", 30, "ml"));
        r8.addIngredient(new RecipeIngredient("Garlic", 2, "cloves"));
        r8.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r8.addIngredient(new RecipeIngredient("Carrots", 2, "units"));
        r8.addIngredient(new RecipeIngredient("Sesame Oil", 15, "ml"));
        recipes.add(r8);

        // 9. Mashed Potatoes (6 ingredients)
        Recipe r9 = new Recipe(9, "Mashed Potatoes", "Side", "Boil peeled potatoes until tender. Drain and mash with butter, warm cream, salt, and black pepper until smooth.", 15, 20);
        r9.addIngredient(new RecipeIngredient("Potatoes", 500, "g"));
        r9.addIngredient(new RecipeIngredient("Butter", 40, "g"));
        r9.addIngredient(new RecipeIngredient("Milk", 100, "ml"));
        r9.addIngredient(new RecipeIngredient("Heavy Cream", 50, "ml"));
        r9.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r9.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r9);

        // 10. Tomato Soup (6 ingredients)
        Recipe r10 = new Recipe(10, "Tomato Soup", "Soup", "Sauté chopped onions and garlic in olive oil. Add chopped tomatoes, broth, salt, pepper. Simmer 20 mins and blend.", 10, 25);
        r10.addIngredient(new RecipeIngredient("Tomatoes", 4, "units"));
        r10.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r10.addIngredient(new RecipeIngredient("Garlic", 2, "cloves"));
        r10.addIngredient(new RecipeIngredient("Vegetable Stock", 250, "ml"));
        r10.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r10.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r10);

        // 11. Garlic Bread (4 ingredients)
        Recipe r11 = new Recipe(11, "Garlic Bread", "Side", "Mix softened butter with minced garlic and salt. Spread over baguette slices and bake at 200°C for 10 mins.", 10, 10);
        r11.addIngredient(new RecipeIngredient("Baguette", 1, "unit"));
        r11.addIngredient(new RecipeIngredient("Butter", 50, "g"));
        r11.addIngredient(new RecipeIngredient("Garlic", 3, "cloves"));
        r11.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r11);

        // 12. Cheesy Omelette (5 ingredients)
        Recipe r12 = new Recipe(12, "Cheesy Omelette", "Breakfast", "Beat eggs with salt. Melt butter in skillet, add bacon, pour eggs, sprinkle shredded cheese on top and fold in half.", 5, 5);
        r12.addIngredient(new RecipeIngredient("Eggs", 3, "units"));
        r12.addIngredient(new RecipeIngredient("Cheddar Cheese", 60, "g"));
        r12.addIngredient(new RecipeIngredient("Butter", 15, "g"));
        r12.addIngredient(new RecipeIngredient("Bacon", 50, "g"));
        r12.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r12);

        // 13. Fried Rice (6 ingredients)
        Recipe r13 = new Recipe(13, "Fried Rice", "Dinner", "Sauté onions, carrots, and garlic in oil. Push aside, scramble eggs, toss in cooked rice and soy sauce until hot.", 10, 12);
        r13.addIngredient(new RecipeIngredient("Rice", 250, "g"));
        r13.addIngredient(new RecipeIngredient("Eggs", 2, "units"));
        r13.addIngredient(new RecipeIngredient("Soy Sauce", 20, "ml"));
        r13.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r13.addIngredient(new RecipeIngredient("Green Peas", 50, "g"));
        r13.addIngredient(new RecipeIngredient("Sesame Oil", 15, "ml"));
        recipes.add(r13);

        // 14. Potato Soup (6 ingredients)
        Recipe r14 = new Recipe(14, "Potato Soup", "Soup", "Sauté chopped onions in butter. Add diced potatoes, water, bacon, salt, pepper. Simmer 20 mins, stir in milk and mash partially.", 10, 25);
        r14.addIngredient(new RecipeIngredient("Potatoes", 400, "g"));
        r14.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r14.addIngredient(new RecipeIngredient("Milk", 150, "ml"));
        r14.addIngredient(new RecipeIngredient("Bacon", 50, "g"));
        r14.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r14.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r14);

        // 15. Crepes (6 ingredients)
        Recipe r15 = new Recipe(15, "Crepes", "Breakfast", "Whisk flour, eggs, milk, sugar, melted butter, vanilla, and salt into a thin batter. Cook thin pancakes in a hot skillet.", 10, 15);
        r15.addIngredient(new RecipeIngredient("Flour", 120, "g"));
        r15.addIngredient(new RecipeIngredient("Milk", 250, "ml"));
        r15.addIngredient(new RecipeIngredient("Eggs", 2, "units"));
        r15.addIngredient(new RecipeIngredient("Vanilla Extract", 5, "ml"));
        r15.addIngredient(new RecipeIngredient("Butter", 15, "g"));
        r15.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r15);

        // 16. Pan-Seared Chicken (5 ingredients)
        Recipe r16 = new Recipe(16, "Pan-Seared Chicken", "Dinner", "Season chicken breast with salt, thyme, and pepper. Sear in olive oil with smashed garlic cloves until golden.", 5, 15);
        r16.addIngredient(new RecipeIngredient("Chicken Breast", 300, "g"));
        r16.addIngredient(new RecipeIngredient("Olive Oil", 15, "ml"));
        r16.addIngredient(new RecipeIngredient("Fresh Thyme", 5, "g"));
        r16.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r16.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r16);

        // 17. Carrot Soup (6 ingredients)
        Recipe r17 = new Recipe(17, "Carrot Soup", "Soup", "Sauté chopped carrots, onions, garlic in olive oil. Add ginger, water, salt, pepper. Simmer until carrots are tender and blend.", 10, 20);
        r17.addIngredient(new RecipeIngredient("Carrots", 4, "units"));
        r17.addIngredient(new RecipeIngredient("Onions", 1, "units"));
        r17.addIngredient(new RecipeIngredient("Ginger", 10, "g"));
        r17.addIngredient(new RecipeIngredient("Olive Oil", 15, "ml"));
        r17.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r17.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r17);

        // 18. Cheesy Pasta (5 ingredients)
        Recipe r18 = new Recipe(18, "Cheesy Pasta", "Dinner", "Boil pasta. Melt butter in pan, add milk, parmesan and cheddar cheese, stir until melted. Toss pasta in sauce.", 5, 12);
        r18.addIngredient(new RecipeIngredient("Pasta", 200, "g"));
        r18.addIngredient(new RecipeIngredient("Cheddar Cheese", 80, "g"));
        r18.addIngredient(new RecipeIngredient("Parmesan Cheese", 40, "g"));
        r18.addIngredient(new RecipeIngredient("Butter", 20, "g"));
        r18.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        recipes.add(r18);

        // 19. Garlic Butter Chicken (6 ingredients)
        Recipe r19 = new Recipe(19, "Garlic Butter Chicken", "Dinner", "Season chicken with salt, paprika and pepper. Sear in olive oil, then add butter and garlic to pan-baste until tender.", 10, 15);
        r19.addIngredient(new RecipeIngredient("Chicken Breast", 300, "g"));
        r19.addIngredient(new RecipeIngredient("Garlic", 3, "cloves"));
        r19.addIngredient(new RecipeIngredient("Butter", 30, "g"));
        r19.addIngredient(new RecipeIngredient("Paprika", 5, "g"));
        r19.addIngredient(new RecipeIngredient("Salt", 1, "pinch"));
        r19.addIngredient(new RecipeIngredient("Black Pepper", 1, "pinch"));
        recipes.add(r19);

        // 20. Sweet Pancakes (5 ingredients)
        Recipe r20 = new Recipe(20, "Sweet Pancakes", "Breakfast", "Mix flour, milk, egg, sugar, vanilla extract, and melted butter into pancake batter. Cook until fluffy.", 10, 10);
        r20.addIngredient(new RecipeIngredient("Flour", 150, "g"));
        r20.addIngredient(new RecipeIngredient("Milk", 200, "ml"));
        r20.addIngredient(new RecipeIngredient("Eggs", 1, "units"));
        r20.addIngredient(new RecipeIngredient("Vanilla Extract", 5, "ml"));
        r20.addIngredient(new RecipeIngredient("Butter", 20, "g"));
        recipes.add(r20);

        return recipes;
    }
}
