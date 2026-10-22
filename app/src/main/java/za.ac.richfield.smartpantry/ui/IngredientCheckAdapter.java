package za.ac.richfield.smartpantry.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.logic.PantrySnapshot;
import za.ac.richfield.smartpantry.model.RecipeIngredient;

public class IngredientCheckAdapter extends RecyclerView.Adapter<IngredientCheckAdapter.CheckViewHolder> {

    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private PantrySnapshot snapshot;

    public void setData(List<RecipeIngredient> ingredients, PantrySnapshot snapshot) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        this.snapshot = snapshot;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_check, parent, false);
        return new CheckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder holder, int position) {
        RecipeIngredient req = ingredients.get(position);
        boolean satisfied = snapshot != null && snapshot.isRequirementSatisfied(req.getIngredientName(), req.getQuantityNeeded(), req.getUnit());

        String reqText = req.getIngredientName() + " (" + formatQuantity(req.getQuantityNeeded()) + " " + req.getUnit() + ")";
        holder.tvReq.setText(reqText);

        if (satisfied) {
            holder.ivIcon.setImageResource(R.drawable.ic_check);
            holder.ivIcon.setColorFilter(Color.parseColor("#388E3C"));
            holder.tvReq.setTextColor(Color.parseColor("#212121"));
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_close);
            holder.ivIcon.setColorFilter(Color.parseColor("#D32F2F"));
            holder.tvReq.setTextColor(Color.parseColor("#D32F2F"));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    private static String formatQuantity(double q) {
        if (q == (long) q) {
            return String.format("%d", (long) q);
        } else {
            return String.format("%.2f", q);
        }
    }

    static class CheckViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvReq;

        CheckViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_check_icon);
            tvReq = itemView.findViewById(R.id.tv_ingredient_req);
        }
    }
}
