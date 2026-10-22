package za.ac.richfield.smartpantry.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.data.DatabaseHelper;
import za.ac.richfield.smartpantry.model.PantryItem;

public class AddEditPantryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private TextInputLayout tilName, tilQuantity;
    private TextInputEditText etName, etQuantity;
    private AutoCompleteTextView actvUnit;
    private TextView tvExpiryDate;
    private Button btnPickExpiry, btnSave, btnDelete;

    private long pantryId = -1;
    private final Calendar expiryCalendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        tilName = findViewById(R.id.til_name);
        tilQuantity = findViewById(R.id.til_quantity);
        etName = findViewById(R.id.et_name);
        etQuantity = findViewById(R.id.et_quantity);
        actvUnit = findViewById(R.id.actv_unit);
        tvExpiryDate = findViewById(R.id.tv_expiry_date);
        btnPickExpiry = findViewById(R.id.btn_pick_expiry_date);
        btnSave = findViewById(R.id.btn_save_pantry_item);
        btnDelete = findViewById(R.id.btn_delete_pantry_item);

        setupUnitDropdown();

        expiryCalendar.add(Calendar.DAY_OF_YEAR, 7);
        updateExpiryText();

        btnPickExpiry.setOnClickListener(v -> showDatePicker());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PantryActivity.EXTRA_PANTRY_ID)) {
            pantryId = intent.getLongExtra(PantryActivity.EXTRA_PANTRY_ID, -1);
        }

        if (pantryId > 0) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_ingredient);
            }
            btnDelete.setVisibility(View.VISIBLE);
            loadItemData();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_ingredient);
            }
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> attemptSave());

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void setupUnitDropdown() {
        String unitPref = dbHelper.getSetting("unit_preference", "metric");
        String[] units;
        if ("household".equalsIgnoreCase(unitPref)) {
            units = new String[]{"units", "cups", "tbsp", "tsp", "g", "ml", "pieces", "slices", "cloves", "cans"};
        } else {
            units = new String[]{"g", "ml", "kg", "l", "units", "pieces", "slices", "cloves", "cans", "cups"};
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units);
        actvUnit.setAdapter(unitAdapter);
        actvUnit.setText(units[0], false);
    }

    private void loadItemData() {
        PantryItem item = dbHelper.getPantryItemById(pantryId);
        if (item != null) {
            etName.setText(item.getName());
            etQuantity.setText(formatQuantity(item.getQuantity()));
            actvUnit.setText(item.getUnit(), false);

            if (item.getExpiryDate() > 0) {
                expiryCalendar.setTimeInMillis(item.getExpiryDate());
                updateExpiryText();
            }
        }
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    expiryCalendar.set(Calendar.YEAR, year);
                    expiryCalendar.set(Calendar.MONTH, month);
                    expiryCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateExpiryText();
                },
                expiryCalendar.get(Calendar.YEAR),
                expiryCalendar.get(Calendar.MONTH),
                expiryCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateExpiryText() {
        tvExpiryDate.setText(getString(R.string.label_expiry) + ": " + dateFormat.format(expiryCalendar.getTime()));
    }

    private void attemptSave() {
        tilName.setError(null);
        tilQuantity.setError(null);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String qtyStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String unit = actvUnit.getText() != null ? actvUnit.getText().toString().trim() : "units";

        if (name.isEmpty()) {
            tilName.setError(getString(R.string.error_empty_name));
            etName.requestFocus();
            return;
        }

        if (name.length() < 2) {
            tilName.setError(getString(R.string.error_short_name));
            etName.requestFocus();
            return;
        }

        if (dbHelper.isDuplicatePantryItem(name, pantryId)) {
            tilName.setError(getString(R.string.error_duplicate_name));
            etName.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                tilQuantity.setError(getString(R.string.error_invalid_quantity));
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            tilQuantity.setError(getString(R.string.error_invalid_quantity));
            etQuantity.requestFocus();
            return;
        }

        long now = System.currentTimeMillis();
        long expiry = expiryCalendar.getTimeInMillis();

        if (pantryId > 0) {
            PantryItem item = new PantryItem(pantryId, name, quantity, unit, now, expiry);
            dbHelper.updatePantryItem(item);
            Toast.makeText(this, "Updated " + name, Toast.LENGTH_SHORT).show();
        } else {
            PantryItem item = new PantryItem(0, name, quantity, unit, now, expiry);
            dbHelper.insertPantryItem(item);
            Toast.makeText(this, "Added " + name, Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void confirmDelete() {
        String currentName = etName.getText() != null ? etName.getText().toString() : "";
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_msg, currentName))
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    if (pantryId > 0) {
                        dbHelper.deletePantryItem(pantryId);
                        Toast.makeText(AddEditPantryActivity.this, "Deleted item", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static String formatQuantity(double q) {
        if (q == (long) q) {
            return String.format(Locale.getDefault(), "%d", (long) q);
        } else {
            return String.format(Locale.getDefault(), "%.2f", q);
        }
    }
}
