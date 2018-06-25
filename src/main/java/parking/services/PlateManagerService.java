package parking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class PlateManagerService {

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
        plateRepository.save(plate);
    }

    public void updatePlateWithGivenId(Long id, Plate plate) {
        plate.setId(id);
        plateRepository.save(plate);
    }

    public void copyStartVipAndStopMeter(Plate plateTo, Plate plateFrom) {
        plateTo.setEnd(Instant.now());
        plateTo.setStart(plateFrom.getStart());
        plateTo.setVip(plateFrom.isVip());
    }
}