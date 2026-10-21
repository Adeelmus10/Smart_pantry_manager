package za.ac.richfield.smartpantry.logic;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IngredientName {

    private static final Set<String> FILLER_WORDS = new HashSet<>(Arrays.asList(
            "ripe", "fresh", "large", "medium", "small", "sliced", "chopped", "diced",
            "raw", "cooked", "dry", "canned", "of", "and", "or", "a", "an", "the"
    ));

    public static String canonicalise(String input) {
        if (input == null) {
            return "";
        }

        String text = input.trim().toLowerCase();
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        text = text.replaceAll("[^a-z0-9]", " ");

        String[] parts = text.split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            // Drop filler words unless it's the only word
            if (FILLER_WORDS.contains(part) && parts.length > 1) {
                continue;
            }
            String deplural = depluralise(part);
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(deplural);
        }

        return sb.toString().trim();
    }

    private static String depluralise(String word) {
        if (word.length() <= 3) {
            return word;
        }
        if (word.endsWith("tomatoes")) return "tomato";
        if (word.endsWith("potatoes")) return "potato";
        if (word.endsWith("berries")) return word.substring(0, word.length() - 3) + "y";
        if (word.endsWith("es") && (word.endsWith("ches") || word.endsWith("shes") || word.endsWith("ves"))) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("es") && word.length() > 4) {
            if (word.endsWith("oes")) {
                return word.substring(0, word.length() - 2);
            }
            return word.substring(0, word.length() - 1);
        }
        if (word.endsWith("s") && !word.endsWith("ss") && !word.endsWith("us") && !word.endsWith("is")) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    public static void main(String[] args) {
        System.out.println("Tomatoes -> " + canonicalise("Tomatoes"));
        System.out.println("TOMATOES   -> " + canonicalise("TOMATOES   "));
        System.out.println("Cherry Tomatoes (ripe) -> " + canonicalise("Cherry Tomatoes (ripe)"));
        System.out.println("cooking oil -> " + canonicalise("cooking oil"));
    }
}
