package parking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PlateManagerService {

    private static final Logger log = LoggerFactory.getLogger(PlateManagerService.class);

    private final PlateRepository plateRepository;

    @Autowired
    public PlateManagerService(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public Optional<Plate> getPlateWithRunningMeter(String plateNr) {
        return plateRepository.findByPlateNr(plateNr).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();
    }

    public void savePlateWithRunningMeter(Instant start, Plate plate) {
        plate.setEnd(null);
        plate.setStart(start);
        log.info("The meter for plate: " + plate.getPlateNr() + " has been started at " + Instant.now());
        plateRepository.save(plate);
    }

    public void updatePlateWithGivenId(Long id, Plate plate) {
        plate.setId(id);
        log.info("The meter for plate: " + plate.getPlateNr() + " has been stopped at " + Instant.now());
        plateRepository.save(plate);
    }

    public void copyStartVipAndStopMeter(Plate plateTo, Plate plateFrom) {
        plateTo.setEnd(Instant.now());
        plateTo.setStart(plateFrom.getStart());
        plateTo.setVip(plateFrom.isVip());
    }

    public BigDecimal getIncome(Instant start ,Instant end) {
        return plateRepository.findByEndBetween(start, end)
                .parallelStream().map(Plate::getPaid).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}