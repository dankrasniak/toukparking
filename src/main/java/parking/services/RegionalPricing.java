package parking.services;

import parking.entities.Plate;

import java.math.BigDecimal;

public abstract class RegionalPricing {

    abstract boolean matchRegion(Plate plate);

    abstract BigDecimal calculatePrice(Plate plate);

    BigDecimal calculatePriceAdequately(long dur, BigDecimal first, BigDecimal second, BigDecimal next) {
        if (dur == 0)
            return first;
        if (dur == 1)
            return second;
        BigDecimal result = second;

        for (int i = 1; i < dur; i++)
            result = result.multiply(next);

        return result;
    }
}
