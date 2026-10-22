package za.ac.richfield.smartpantry.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.logic.ExpiryCalendar;
import za.ac.richfield.smartpantry.model.PantryItem;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private List<PantryItem> items = new ArrayList<>();
    private final OnItemClickListener listener;
    private boolean showExpiryAlerts = true;

    public PantryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setShowExpiryAlerts(boolean showExpiryAlerts) {
        this.showExpiryAlerts = showExpiryAlerts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.tvName.setText(item.getName());

        String qtyText = formatQuantity(item.getQuantity()) + " " + item.getUnit();
        holder.tvQuantity.setText(qtyText);

        if (showExpiryAlerts && item.getExpiryDate() > 0 && ExpiryCalendar.isExpiringSoon(item.getExpiryDate(), 3)) {
            holder.tvExpiryBadge.setVisibility(View.VISIBLE);
            holder.tvExpiryBadge.setText(ExpiryCalendar.getExpiryText(item.getExpiryDate()));
        } else {
            holder.tvExpiryBadge.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static String formatQuantity(double q) {
        if (q == (long) q) {
            return String.format("%d", (long) q);
        } else {
            return String.format("%.2f", q);
        }
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiryBadge;
        ImageButton btnDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvExpiryBadge = itemView.findViewById(R.id.tv_expiry_badge);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}
