package parking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.entities.Plate;

import java.rmi.activation.UnknownObjectException;
import java.util.Collection;
import java.util.Optional;

@Service
public class Pricing {

    private final Collection<RegionalPricing> regionalPricing;

    @Autowired
    public Pricing(Collection<RegionalPricing> regionalPricing) {
        this.regionalPricing = regionalPricing;
    }

    public void updatePrice(Plate plate) throws UnknownObjectException {

        Optional<RegionalPricing> searchResult = regionalPricing.parallelStream()
                .filter(r -> r.matchRegion(plate)).findAny();

        if (!searchResult.isPresent())
            throw new UnknownObjectException(plate.getRegion().toString());

        plate.setPaid(searchResult.get().calculatePrice(plate));
    }
}
