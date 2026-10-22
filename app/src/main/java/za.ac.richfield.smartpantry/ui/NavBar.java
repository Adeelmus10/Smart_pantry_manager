package za.ac.richfield.smartpantry.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import za.ac.richfield.smartpantry.R;

public class NavBar {

    public static final int NAV_PANTRY = 1;
    public static final int NAV_SUGGESTIONS = 2;
    public static final int NAV_SETTINGS = 3;

    public static void setup(Activity activity, int activeItem) {
        View btnPantry = activity.findViewById(R.id.nav_btn_pantry);
        View btnSuggestions = activity.findViewById(R.id.nav_btn_suggestions);
        View btnSettings = activity.findViewById(R.id.nav_btn_settings);

        ImageView iconPantry = activity.findViewById(R.id.nav_icon_pantry);
        ImageView iconSuggestions = activity.findViewById(R.id.nav_icon_suggestions);
        ImageView iconSettings = activity.findViewById(R.id.nav_icon_settings);

        TextView textPantry = activity.findViewById(R.id.nav_text_pantry);
        TextView textSuggestions = activity.findViewById(R.id.nav_text_suggestions);
        TextView textSettings = activity.findViewById(R.id.nav_text_settings);

        int activeColor = ContextCompat.getColor(activity, R.color.primary_dark);
        int inactiveColor = Color.parseColor("#64748B");

        if (iconPantry != null && textPantry != null) {
            boolean active = activeItem == NAV_PANTRY;
            iconPantry.setColorFilter(active ? activeColor : inactiveColor);
            textPantry.setTextColor(active ? activeColor : inactiveColor);
        }

        if (iconSuggestions != null && textSuggestions != null) {
            boolean active = activeItem == NAV_SUGGESTIONS;
            iconSuggestions.setColorFilter(active ? activeColor : inactiveColor);
            textSuggestions.setTextColor(active ? activeColor : inactiveColor);
        }

        if (iconSettings != null && textSettings != null) {
            boolean active = activeItem == NAV_SETTINGS;
            iconSettings.setColorFilter(active ? activeColor : inactiveColor);
            textSettings.setTextColor(active ? activeColor : inactiveColor);
        }

        if (btnPantry != null) {
            btnPantry.setOnClickListener(v -> {
                if (activeItem != NAV_PANTRY) {
                    Intent intent = new Intent(activity, PantryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    activity.startActivity(intent);
                }
            });
        }

        if (btnSuggestions != null) {
            btnSuggestions.setOnClickListener(v -> {
                if (activeItem != NAV_SUGGESTIONS) {
                    Intent intent = new Intent(activity, SuggestedRecipesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    activity.startActivity(intent);
                }
            });
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                if (activeItem != NAV_SETTINGS) {
                    Intent intent = new Intent(activity, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
