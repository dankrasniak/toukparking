package parking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.entities.Plate;

import java.rmi.activation.UnknownObjectException;
import java.util.Collection;
import java.util.Optional;

@Service
public class PricingService {

    private final Collection<AbstractRegionPricing> abstractRegionPricing;

    @Autowired
    public PricingService(Collection<AbstractRegionPricing> abstractRegionPricing) {
        this.abstractRegionPricing = abstractRegionPricing;
    }

    public void updatePrice(Plate plate) throws UnknownObjectException {

        Optional<AbstractRegionPricing> searchResult = abstractRegionPricing.parallelStream()
                .filter(r -> r.matchRegion(plate)).findAny();

        if (!searchResult.isPresent())
            throw new UnknownObjectException(plate.getRegion().toString());

        plate.setPaid(searchResult.get().calculatePrice(plate));
    }
}
