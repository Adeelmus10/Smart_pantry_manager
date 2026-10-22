package za.ac.richfield.smartpantry.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private long recipeId = -1;
    private Recipe recipe;

    private TextView tvTitle, tvTimeCategory, tvStatusBanner, tvInstructions;
    private RecyclerView rvIngredients;
    private IngredientCheckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_recipe_detail);
        }

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tv_detail_title);
        tvTimeCategory = findViewById(R.id.tv_detail_time_category);
        tvStatusBanner = findViewById(R.id.tv_detail_status_banner);
        tvInstructions = findViewById(R.id.tv_detail_instructions);
        rvIngredients = findViewById(R.id.rv_recipe_ingredients);

        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientCheckAdapter();
        rvIngredients.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            recipeId = intent.getLongExtra(SuggestedRecipesActivity.EXTRA_RECIPE_ID, -1);
        }

        if (recipeId <= 0) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipeDetail();
    }

    private void loadRecipeDetail() {
        recipe = dbHelper.getRecipeById(recipeId);
        if (recipe == null) {
            finish();
            return;
        }

        tvTitle.setText(recipe.getName());
        int totalTime = recipe.getPrepTimeMinutes() + recipe.getCookTimeMinutes();
        String timeCategoryText = recipe.getCategory() + " • " + totalTime + " mins total (Prep: " + recipe.getPrepTimeMinutes() + "m, Cook: " + recipe.getCookTimeMinutes() + "m)";
        tvTimeCategory.setText(timeCategoryText);

        tvInstructions.setText(recipe.getInstructions());

        List<PantryItem> pantryItems = dbHelper.getAllPantryItems();
        PantrySnapshot snapshot = PantrySnapshot.fromList(pantryItems);

        boolean isMatched = RecipeMatchEngine.evaluate(recipe, snapshot);

        if (isMatched) {
            tvStatusBanner.setText(R.string.status_suggested);
            tvStatusBanner.setBackgroundResource(R.drawable.bg_green_badge);
            tvStatusBanner.setTextColor(Color.parseColor("#388E3C"));
        } else {
            List<String> missing = recipe.getMissingIngredients();
            String missingText = missing.isEmpty() ? "ingredients" : String.join(", ", missing);
            tvStatusBanner.setText(getString(R.string.status_not_suggested, missingText));
            tvStatusBanner.setBackgroundResource(R.drawable.bg_red_badge);
            tvStatusBanner.setTextColor(Color.parseColor("#D32F2F"));
        }

        adapter.setData(recipe.getIngredients(), snapshot);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
