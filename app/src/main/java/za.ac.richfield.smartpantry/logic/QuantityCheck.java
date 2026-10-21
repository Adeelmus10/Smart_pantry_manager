package za.ac.richfield.smartpantry.logic;

public class QuantityCheck {

    public static boolean isSatisfied(double availableAmount, String availableUnit,
                                       double neededAmount, String neededUnit) {
        String dimAvail = UnitConverter.getDimension(availableUnit);
        String dimNeed = UnitConverter.getDimension(neededUnit);

        double baseAvail = UnitConverter.toBaseUnitQuantity(availableAmount, availableUnit);
        double baseNeed = UnitConverter.toBaseUnitQuantity(neededAmount, neededUnit);

        if (dimAvail.equals(dimNeed)) {
            if (UnitConverter.DIMENSION_MASS.equals(dimAvail) || UnitConverter.DIMENSION_VOLUME.equals(dimAvail)) {
                return baseAvail >= (baseNeed * 0.98);
            } else {
                return baseAvail >= baseNeed;
            }
        }

        return baseAvail >= baseNeed;
    }
}
