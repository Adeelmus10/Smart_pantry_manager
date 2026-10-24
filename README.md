# Smart Pantry Manager

Smart Pantry Manager is an Android app built in Java for the **Mobile App Development 700**
practical assignment at Richfield Graduate Institute of Technology. It lets users keep a list of
pantry ingredients and find recipes that can be made with the ingredients and quantities on hand.

---

## Features

| Screen | What it does |
| --- | --- |
| `PantryActivity` | Shows pantry ingredients, quantities and expiry warnings. |
| `AddEditPantryActivity` | Adds, edits and deletes pantry items, with input validation. |
| `SuggestedRecipesActivity` | Shows recipes whose ingredients and quantities are available. |
| `RecipeDetailActivity` | Shows a recipe's ingredients, availability and method. |
| `SettingsActivity` | Includes expiry alerts, unit preferences and demo-data actions. |

Recipe matching requires every listed ingredient. Ingredient names are normalised before
comparison, so names such as `tomatoes` and `Tomato` can match. Compatible units are converted
before quantities are checked (for example, `0.5 kg` and `500 g`). Counted items such as eggs,
slices and cans are compared as counts.

The app has no requested permissions and works offline. The database is seeded with 20 recipes
and 107 ingredient requirements on first launch.

## Stack

* Java 11, Android Studio and Android Gradle Plugin 8.5.2 (`compileSdk 34`, `minSdk 24`)
* SQLite through `SQLiteOpenHelper`, with four tables and on-device storage
* Activities, Intents, `RecyclerView` and custom adapters
* AndroidX and Material libraries; no other third-party libraries

## Database: why SQLite

The app stores data for one user on one device, so it does not need a remote database or
synchronisation. SQLite keeps the data available between launches and represents the relationship
between recipes and their ingredient requirements.

## Getting it running

1. Install **Android Studio** (Hedgehog or newer), JDK 17 and the Android SDK.
2. Open this folder in Android Studio and wait for Gradle to sync.
   The Gradle wrapper JAR is not included. If prompted, generate the wrapper with
   `gradle wrapper --gradle-version 8.7`.
3. Start an emulator running API 24 or newer, or connect a phone with USB debugging enabled.
4. Select **Run ▸ Run 'app'**. On first launch, the app seeds the recipes and adds an 18-item
   starter pantry. Use **Settings ▸ Delete every ingredient** to clear the pantry.
5. To check recipe matching, open **Suggestions**, remove **Butter** from the pantry, and check
   the results. Recipes requiring butter should no longer appear. Add it back to see them again.

## Layout of the project

```
app/src/main/java/za/ac/richfield/smartpantry/
├── data/    DatabaseHelper (SQLiteOpenHelper + all SQL), RecipeSeedData, StarterPantry
├── model/   PantryItem, Recipe, RecipeIngredient - plain beans, no Android imports
├── logic/   IngredientName, UnitConverter, QuantityCheck, PantrySnapshot, RecipeMatchEngine,
│            ExpiryCalendar - the rules; no Android imports, so they run on a plain JVM
└── ui/      5 Activities, 3 RecyclerView adapters, NavBar (the bottom navigation strip)
app/src/main/res/
├── layout/  5 activity layouts, 3 row layouts, view_bottom_nav.xml
├── drawable/ vector icons and the launcher mark
└── values/  strings, colours, themes
tools/verify.sh + tools/VerifyLogic.java  - the offline test harness (see below)
docs/      commit plan, video script, screenshot guide
```

## Testing without an emulator

The `logic/` and `model/` packages do not use Android imports, so the matching logic can be tested
from a terminal:

```bash
./tools/verify.sh
```

The script compiles those packages and runs 23 checks against the seed data. These cover missing
ingredients, quantity shortfalls and changes to recipe matches when pantry items are deleted. A
JDK is required.

## Implementation notes

* `RecipeMatchEngine.evaluate()` handles recipe matching.
* `DatabaseHelper` contains the database schema, indexes, seed data and CRUD operations.
* `AddEditPantryActivity.attemptSave()` validates pantry item input.
* `AndroidManifest.xml` does not declare any permissions.

