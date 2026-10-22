package za.ac.richfield.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.data.DatabaseHelper;
import za.ac.richfield.smartpantry.model.PantryItem;

public class PantryActivity extends AppCompatActivity implements PantryAdapter.OnItemClickListener {

    public static final String EXTRA_PANTRY_ID = "pantry_id";

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private TextView tvEmpty;
    private RecyclerView rvPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_pantry);
        }

        dbHelper = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tv_empty_pantry);
        rvPantry = findViewById(R.id.rv_pantry);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_item);

        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        rvPantry.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, AddEditPantryActivity.class);
            startActivity(intent);
        });

        NavBar.setup(this, NavBar.NAV_PANTRY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavBar.setup(this, NavBar.NAV_PANTRY);
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        boolean alertsEnabled = Boolean.parseBoolean(dbHelper.getSetting("alerts_enabled", "true"));

        adapter.setItems(items);
        adapter.setShowExpiryAlerts(alertsEnabled);

        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvPantry.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(PantryActivity.this, AddEditPantryActivity.class);
        intent.putExtra(EXTRA_PANTRY_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_msg, item.getName()))
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    dbHelper.deletePantryItem(item.getId());
                    Toast.makeText(PantryActivity.this, "Deleted " + item.getName(), Toast.LENGTH_SHORT).show();
                    loadPantryItems();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
