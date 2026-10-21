package za.ac.richfield.smartpantry.logic;

public class UnitConverter {

    public static final String DIMENSION_MASS = "mass";
    public static final String DIMENSION_VOLUME = "volume";
    public static final String DIMENSION_COUNT = "count";

    public static String getDimension(String unit) {
        if (unit == null) {
            return DIMENSION_COUNT;
        }
        String u = unit.trim().toLowerCase();
        switch (u) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kilogram":
            case "kilograms":
            case "mg":
            case "oz":
            case "ounce":
            case "ounces":
            case "lb":
            case "lbs":
            case "pound":
            case "pounds":
                return DIMENSION_MASS;

            case "ml":
            case "milliliter":
            case "milliliters":
            case "l":
            case "liter":
            case "liters":
            case "tsp":
            case "teaspoon":
            case "teaspoons":
            case "tbsp":
            case "tablespoon":
            case "tablespoons":
            case "cup":
            case "cups":
            case "fl oz":
                return DIMENSION_VOLUME;

            default:
                return DIMENSION_COUNT;
        }
    }

    public static double toBaseUnitQuantity(double amount, String unit) {
        if (unit == null) {
            return amount;
        }
        String u = unit.trim().toLowerCase();
        switch (u) {
            case "g":
            case "gram":
            case "grams":
                return amount;
            case "kg":
            case "kilogram":
            case "kilograms":
                return amount * 1000.0;
            case "mg":
                return amount / 1000.0;
            case "oz":
            case "ounce":
            case "ounces":
                return amount * 28.3495;
            case "lb":
            case "lbs":
            case "pound":
            case "pounds":
                return amount * 453.592;

            case "ml":
            case "milliliter":
            case "milliliters":
                return amount;
            case "l":
            case "liter":
            case "liters":
                return amount * 1000.0;
            case "tsp":
            case "teaspoon":
            case "teaspoons":
                return amount * 5.0;
            case "tbsp":
            case "tablespoon":
            case "tablespoons":
                return amount * 15.0;
            case "cup":
            case "cups":
                return amount * 250.0;
            case "fl oz":
                return amount * 29.5735;

            default:
                return amount;
        }
    }

    public static boolean areCompatible(String unit1, String unit2) {
        return getDimension(unit1).equals(getDimension(unit2));
    }
}
