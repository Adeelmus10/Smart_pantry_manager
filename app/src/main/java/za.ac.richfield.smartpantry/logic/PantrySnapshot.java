package za.ac.richfield.smartpantry.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.ac.richfield.smartpantry.model.PantryItem;

public class PantrySnapshot {

    private final Map<String, Map<String, Double>> pooledPantry = new HashMap<>();

    private PantrySnapshot() {
    }

    public static PantrySnapshot fromList(List<PantryItem> items) {
        PantrySnapshot snapshot = new PantrySnapshot();
        if (items != null) {
            for (PantryItem item : items) {
                snapshot.addPantryItem(item);
            }
        }
        return snapshot;
    }

    private void addPantryItem(PantryItem item) {
        if (item == null || item.getName() == null) {
            return;
        }
        String key = IngredientName.canonicalise(item.getName());
        if (key.isEmpty()) {
            return;
        }

        String dim = UnitConverter.getDimension(item.getUnit());
        double baseQty = UnitConverter.toBaseUnitQuantity(item.getQuantity(), item.getUnit());

        Map<String, Double> dimMap = pooledPantry.computeIfAbsent(key, k -> new HashMap<>());
        double current = dimMap.containsKey(dim) ? dimMap.get(dim) : 0.0;
        dimMap.put(dim, current + baseQty);
    }

    public boolean isRequirementSatisfied(String requiredIngredientName, double requiredQuantity, String requiredUnit) {
        if (requiredIngredientName == null || pooledPantry.isEmpty()) {
            return false;
        }

        String reqKey = IngredientName.canonicalise(requiredIngredientName);
        if (reqKey.isEmpty()) {
            return false;
        }

        Map<String, Double> dimMap = lookup(reqKey);
        if (dimMap == null || dimMap.isEmpty()) {
            return false;
        }

        String reqDim = UnitConverter.getDimension(requiredUnit);
        Double availBase = dimMap.get(reqDim);

        if (availBase == null) {
            for (Double val : dimMap.values()) {
                if (val != null && QuantityCheck.isSatisfied(val, getBaseUnitForDim(reqDim), requiredQuantity, requiredUnit)) {
                    return true;
                }
            }
            return false;
        }

        String baseUnit = getBaseUnitForDim(reqDim);
        return QuantityCheck.isSatisfied(availBase, baseUnit, requiredQuantity, requiredUnit);
    }

    private Map<String, Double> lookup(String reqKey) {
        if (pooledPantry.containsKey(reqKey)) {
            return pooledPantry.get(reqKey);
        }

        for (Map.Entry<String, Map<String, Double>> entry : pooledPantry.entrySet()) {
            String pantryKey = entry.getKey();
            if (pantryKey.equals(reqKey)) {
                return entry.getValue();
            }
            if (reqKey.equals("oil") && (pantryKey.endsWith(" oil") || pantryKey.contains("oil"))) {
                return entry.getValue();
            }
            if (pantryKey.equals("oil") && (reqKey.endsWith(" oil") || reqKey.contains("oil"))) {
                return entry.getValue();
            }
            if (pantryKey.contains(reqKey) || reqKey.contains(pantryKey)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private static String getBaseUnitForDim(String dim) {
        if (UnitConverter.DIMENSION_MASS.equals(dim)) return "g";
        if (UnitConverter.DIMENSION_VOLUME.equals(dim)) return "ml";
        return "units";
    }
}
