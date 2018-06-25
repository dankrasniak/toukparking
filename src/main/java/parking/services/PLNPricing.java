package parking.services;

import org.springframework.stereotype.Service;
import parking.entities.Plate;
import parking.entities.enums.Region;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@Service
public class PLNPricing extends RegionalPricing {

    private static final BigDecimal FIRST_HOUR_VIP = BigDecimal.valueOf(0);
    private static final BigDecimal SECOND_HOUR_VIP = BigDecimal.valueOf(2);
    private static final BigDecimal NEXT_HOUR_VIP = BigDecimal.valueOf(1.2);

    private static final BigDecimal FIRST_HOUR = BigDecimal.valueOf(1);
    private static final BigDecimal SECOND_HOUR = BigDecimal.valueOf(2);
    private static final BigDecimal NEXT_HOUR = BigDecimal.valueOf(1.5);

    @Override
    public boolean matchRegion(Plate plate) {
        return Objects.equals(Region.PLN, plate.getRegion());
    }

    @Override
    public BigDecimal calculatePrice(Plate plate) {
        long duration = Duration.between(plate.getEnd(), plate.getStart()).toHours();

        if (plate.isVip())
            return calculatePriceAdequately(duration, FIRST_HOUR_VIP, SECOND_HOUR_VIP, NEXT_HOUR_VIP);
        return calculatePriceAdequately(duration, FIRST_HOUR, SECOND_HOUR, NEXT_HOUR);
    }
}
