package parking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parking.entities.Plate;

import java.time.Instant;
import java.util.Collection;

public interface PlateRepository extends JpaRepository<Plate, Long> {
    Collection<Plate> findByPlateNr(String plateNr);
    Collection<Plate> findByEndBetween(Instant start, Instant end);
}
