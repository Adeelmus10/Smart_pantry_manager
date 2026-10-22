package za.ac.richfield.smartpantry.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import za.ac.richfield.smartpantry.R;
import za.ac.richfield.smartpantry.data.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private SwitchCompat switchAlerts;
    private RadioGroup rgUnits;
    private RadioButton rbMetric, rbHousehold;
    private Button btnLoadStarter, btnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_settings);
        }

        dbHelper = new DatabaseHelper(this);

        switchAlerts = findViewById(R.id.switch_expiry_alerts);
        rgUnits = findViewById(R.id.rg_unit_preference);
        rbMetric = findViewById(R.id.rb_unit_metric);
        rbHousehold = findViewById(R.id.rb_unit_household);
        btnLoadStarter = findViewById(R.id.btn_load_starter_pantry);
        btnDeleteAll = findViewById(R.id.btn_delete_all_pantry);

        loadSettings();

        switchAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.setSetting("alerts_enabled", String.valueOf(isChecked));
        });

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_unit_household) {
                dbHelper.setSetting("unit_preference", "household");
            } else {
                dbHelper.setSetting("unit_preference", "metric");
            }
        });

        btnLoadStarter.setOnClickListener(v -> confirmLoadStarter());

        btnDeleteAll.setOnClickListener(v -> confirmDeleteAll());

        NavBar.setup(this, NavBar.NAV_SETTINGS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavBar.setup(this, NavBar.NAV_SETTINGS);
    }

    private void loadSettings() {
        boolean alertsEnabled = Boolean.parseBoolean(dbHelper.getSetting("alerts_enabled", "true"));
        switchAlerts.setChecked(alertsEnabled);

        String unitPref = dbHelper.getSetting("unit_preference", "metric");
        if ("household".equalsIgnoreCase(unitPref)) {
            rbHousehold.setChecked(true);
        } else {
            rbMetric.setChecked(true);
        }
    }

    private void confirmLoadStarter() {
        new AlertDialog.Builder(this)
                .setTitle("Restore Starter Pantry")
                .setMessage("This will clear your current ingredients and load the 18-item starter pantry. Continue?")
                .setPositiveButton("Restore", (dialog, which) -> {
                    dbHelper.restoreStarterPantry();
                    Toast.makeText(SettingsActivity.this, R.string.toast_starter_loaded, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Every Ingredient")
                .setMessage("Are you sure you want to remove ALL ingredients from your pantry? This cannot be undone.")
                .setPositiveButton("Delete All", (dialog, which) -> {
                    dbHelper.clearAllPantryItems();
                    Toast.makeText(SettingsActivity.this, R.string.toast_pantry_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
