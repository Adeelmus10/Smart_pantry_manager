package za.ac.richfield.smartpantry.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes != null ? recipes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvName.setText(recipe.getName());
        holder.tvCategory.setText(recipe.getCategory());

        int totalTime = recipe.getPrepTimeMinutes() + recipe.getCookTimeMinutes();
        holder.tvTime.setText(totalTime + " mins");

        holder.tvIngredientsCount.setText(recipe.getIngredientCount() + " ingredients");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvTime, tvIngredientsCount;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_recipe_name);
            tvCategory = itemView.findViewById(R.id.tv_recipe_category);
            tvTime = itemView.findViewById(R.id.tv_recipe_time);
            tvIngredientsCount = itemView.findViewById(R.id.tv_recipe_ingredients_count);
        }
    }
}
