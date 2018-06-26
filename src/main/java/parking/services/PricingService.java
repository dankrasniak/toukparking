package parking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.entities.DateContainer;
import parking.entities.Plate;

import java.rmi.activation.UnknownObjectException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

@Service
public class PricingService {

    private final Collection<AbstractRegionPricing> abstractRegionPricing;
    private final PlateManagerService plateManagerService;

    @Autowired
    public PricingService(Collection<AbstractRegionPricing> abstractRegionPricing, PlateManagerService plateManagerService) {
        this.abstractRegionPricing = abstractRegionPricing;
        this.plateManagerService = plateManagerService;
    }

    public void updatePrice(Plate plate) throws UnknownObjectException {

        Optional<AbstractRegionPricing> searchResult = abstractRegionPricing.parallelStream()
                .filter(r -> r.matchRegion(plate)).findAny();

        if (!searchResult.isPresent())
            throw new UnknownObjectException(plate.getRegion().toString());

        plate.setPaid(searchResult.get().calculatePrice(plate));
    }

    public double getIncomeForDay(DateContainer localDate) {
        Instant start = localDate.getDateTime().atStartOfDay(ZoneId.systemDefault()).toInstant();
        return plateManagerService.getIncome(start, start.plus(Duration.ofDays(1))).doubleValue();
    }
}
