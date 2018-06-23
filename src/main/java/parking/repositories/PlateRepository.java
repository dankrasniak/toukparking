package parking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parking.entities.Plate;

import java.util.Optional;

public interface PlateRepository extends JpaRepository<Plate, Long> {
    Optional<Plate> findByPlateNr(String plateNr);
}
