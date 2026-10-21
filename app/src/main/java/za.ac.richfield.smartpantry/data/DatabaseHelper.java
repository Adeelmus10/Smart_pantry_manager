package za.ac.richfield.smartpantry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import za.ac.richfield.smartpantry.logic.IngredientName;
import za.ac.richfield.smartpantry.model.PantryItem;
import za.ac.richfield.smartpantry.model.Recipe;
import za.ac.richfield.smartpantry.model.RecipeIngredient;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String TABLE_SETTINGS = "app_settings";

    // Column names - pantry
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_NAME_KEY = "name_key";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_PURCHASE_DATE = "purchase_date";
    public static final String COL_PANTRY_EXPIRY_DATE = "expiry_date";

    // Column names - settings
    public static final String COL_SETTING_KEY = "setting_key";
    public static final String COL_SETTING_VALUE = "setting_value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_NAME_KEY + " TEXT NOT NULL, " +
                COL_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT NOT NULL, " +
                COL_PANTRY_PURCHASE_DATE + " INTEGER NOT NULL, " +
                COL_PANTRY_EXPIRY_DATE + " INTEGER NOT NULL" +
                ");";

        String createIndexPantry = "CREATE INDEX idx_pantry_name_key ON " + TABLE_PANTRY + "(" + COL_PANTRY_NAME_KEY + ");";

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "instructions TEXT NOT NULL, " +
                "prep_time INTEGER NOT NULL, " +
                "cook_time INTEGER NOT NULL" +
                ");";

        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "ingredient_name TEXT NOT NULL, " +
                "quantity_needed REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + TABLE_RECIPES + "(id) ON DELETE CASCADE" +
                ");";

        String createIndexIngredients = "CREATE INDEX idx_recipe_ingredients_recipe_id ON " + TABLE_RECIPE_INGREDIENTS + "(recipe_id);";

        String createSettings = "CREATE TABLE " + TABLE_SETTINGS + " (" +
                COL_SETTING_KEY + " TEXT PRIMARY KEY, " +
                COL_SETTING_VALUE + " TEXT NOT NULL" +
                ");";

        db.execSQL(createPantry);
        db.execSQL(createIndexPantry);
        db.execSQL(createRecipes);
        db.execSQL(createRecipeIngredients);
        db.execSQL(createIndexIngredients);
        db.execSQL(createSettings);

        seedRecipes(db);
        seedStarterPantry(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    public void seedRecipes(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            List<Recipe> recipes = RecipeSeedData.buildAll();
            for (Recipe recipe : recipes) {
                ContentValues cvRecipe = new ContentValues();
                cvRecipe.put("name", recipe.getName());
                cvRecipe.put("category", recipe.getCategory());
                cvRecipe.put("instructions", recipe.getInstructions());
                cvRecipe.put("prep_time", recipe.getPrepTimeMinutes());
                cvRecipe.put("cook_time", recipe.getCookTimeMinutes());

                long recipeId = db.insert(TABLE_RECIPES, null, cvRecipe);

                for (RecipeIngredient ingredient : recipe.getIngredients()) {
                    ContentValues cvIng = new ContentValues();
                    cvIng.put("recipe_id", recipeId);
                    cvIng.put("ingredient_name", ingredient.getIngredientName());
                    cvIng.put("quantity_needed", ingredient.getQuantityNeeded());
                    cvIng.put("unit", ingredient.getUnit());
                    db.insert(TABLE_RECIPE_INGREDIENTS, null, cvIng);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void seedStarterPantry(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            String[][] rows = StarterPantry.items();
            long now = System.currentTimeMillis();
            Calendar expCal = Calendar.getInstance();

            for (int i = 0; i < rows.length; i++) {
                String name = rows[i][0];
                double qty = Double.parseDouble(rows[i][1]);
                String unit = rows[i][2];

                expCal.setTimeInMillis(now);
                expCal.add(Calendar.DAY_OF_YEAR, 7 + (i * 2));

                ContentValues cv = new ContentValues();
                cv.put(COL_PANTRY_NAME, name);
                cv.put(COL_PANTRY_NAME_KEY, IngredientName.canonicalise(name));
                cv.put(COL_PANTRY_QUANTITY, qty);
                cv.put(COL_PANTRY_UNIT, unit);
                cv.put(COL_PANTRY_PURCHASE_DATE, now);
                cv.put(COL_PANTRY_EXPIRY_DATE, expCal.getTimeInMillis());

                db.insert(TABLE_PANTRY, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // --- Pantry CRUD ---

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");

        if (cursor != null) {
            try {
                int idIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_NAME);
                int qtyIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_QUANTITY);
                int unitIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT);
                int purchIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_PURCHASE_DATE);
                int expIdx = cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY_DATE);

                while (cursor.moveToNext()) {
                    PantryItem item = new PantryItem(
                            cursor.getLong(idIdx),
                            cursor.getString(nameIdx),
                            cursor.getDouble(qtyIdx),
                            cursor.getString(unitIdx),
                            cursor.getLong(purchIdx),
                            cursor.getLong(expIdx)
                    );
                    items.add(item);
                }
            } finally {
                cursor.close();
            }
        }
        return items;
    }

    public PantryItem getPantryItemById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return new PantryItem(
                            cursor.getLong(cursor.getColumnIndexOrThrow(COL_PANTRY_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QUANTITY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(COL_PANTRY_PURCHASE_DATE)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY_DATE))
                    );
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public long insertPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, item.getName());
        cv.put(COL_PANTRY_NAME_KEY, IngredientName.canonicalise(item.getName()));
        cv.put(COL_PANTRY_QUANTITY, item.getQuantity());
        cv.put(COL_PANTRY_UNIT, item.getUnit());
        cv.put(COL_PANTRY_PURCHASE_DATE, item.getPurchaseDate());
        cv.put(COL_PANTRY_EXPIRY_DATE, item.getExpiryDate());

        return db.insert(TABLE_PANTRY, null, cv);
    }

    public boolean updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, item.getName());
        cv.put(COL_PANTRY_NAME_KEY, IngredientName.canonicalise(item.getName()));
        cv.put(COL_PANTRY_QUANTITY, item.getQuantity());
        cv.put(COL_PANTRY_UNIT, item.getUnit());
        cv.put(COL_PANTRY_PURCHASE_DATE, item.getPurchaseDate());
        cv.put(COL_PANTRY_EXPIRY_DATE, item.getExpiryDate());

        int count = db.update(TABLE_PANTRY, cv, COL_PANTRY_ID + "=?", new String[]{String.valueOf(item.getId())});
        return count > 0;
    }

    public boolean deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        return count > 0;
    }

    public void clearAllPantryItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, null, null);
    }

    public boolean isDuplicatePantryItem(String name, long excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String key = IngredientName.canonicalise(name);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, new String[]{COL_PANTRY_ID},
                COL_PANTRY_NAME_KEY + "=? AND " + COL_PANTRY_ID + "!=?",
                new String[]{key, String.valueOf(excludeId)}, null, null, null);

        if (cursor != null) {
            try {
                return cursor.getCount() > 0;
            } finally {
                cursor.close();
            }
        }
        return false;
    }

    // --- Recipes ---

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, "name ASC");

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                    Recipe recipe = new Recipe(
                            recipeId,
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("category")),
                            cursor.getString(cursor.getColumnIndexOrThrow("instructions")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("cook_time"))
                    );
                    recipe.setIngredients(getRecipeIngredients(db, recipeId));
                    recipes.add(recipe);
                }
            } finally {
                cursor.close();
            }
        }
        return recipes;
    }

    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                    Recipe recipe = new Recipe(
                            recipeId,
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("category")),
                            cursor.getString(cursor.getColumnIndexOrThrow("instructions")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("cook_time"))
                    );
                    recipe.setIngredients(getRecipeIngredients(db, recipeId));
                    return recipe;
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private List<RecipeIngredient> getRecipeIngredients(SQLiteDatabase db, long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null, "recipe_id=?",
                new String[]{String.valueOf(recipeId)}, null, null, "id ASC");

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    ingredients.add(new RecipeIngredient(
                            cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("recipe_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("quantity_needed")),
                            cursor.getString(cursor.getColumnIndexOrThrow("unit"))
                    ));
                }
            } finally {
                cursor.close();
            }
        }
        return ingredients;
    }

    // --- Settings ---

    public String getSetting(String key, String defaultValue) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETTINGS, new String[]{COL_SETTING_VALUE},
                COL_SETTING_KEY + "=?", new String[]{key}, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow(COL_SETTING_VALUE));
                }
            } finally {
                cursor.close();
            }
        }
        return defaultValue;
    }

    public void setSetting(String key, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SETTING_KEY, key);
        cv.put(COL_SETTING_VALUE, value);
        db.insertWithOnConflict(TABLE_SETTINGS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void restoreStarterPantry() {
        SQLiteDatabase db = getWritableDatabase();
        clearAllPantryItems();
        seedStarterPantry(db);
    }
}
