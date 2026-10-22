package za.ac.richfield.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.data.DatabaseHelper;
import za.ac.richfield.smartpantry.logic.PantrySnapshot;
import za.ac.richfield.smartpantry.logic.RecipeMatchEngine;
import za.ac.richfield.smartpantry.model.PantryItem;
import za.ac.richfield.smartpantry.model.Recipe;

public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private TextView tvEmpty;
    private RecyclerView rvRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_suggestions);
        }

        dbHelper = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tv_empty_suggestions);
        rvRecipes = findViewById(R.id.rv_suggested_recipes);

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        rvRecipes.setAdapter(adapter);

        NavBar.setup(this, NavBar.NAV_SUGGESTIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavBar.setup(this, NavBar.NAV_SUGGESTIONS);
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();
        PantrySnapshot snapshot = PantrySnapshot.fromList(pantryItems);

        List<Recipe> suggestions = RecipeMatchEngine.suggest(allRecipes, snapshot);

        adapter.setRecipes(suggestions);

        if (suggestions.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
