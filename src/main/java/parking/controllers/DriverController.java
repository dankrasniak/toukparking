package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import java.time.Instant;
import java.util.Optional;

@Controller
public class DriverController {

    private final PlateRepository plateRepository;

    @Autowired
    public DriverController(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("plate", new Plate());
        return "driver";
    }

    @PostMapping("/savePlate")
    public String savePlateSubmit(@ModelAttribute Plate plate, Model model) {
        Instant now = Instant.now();
        plate.setEnd(now);
        model.addAttribute("plate", plate);
        model.addAttribute("plateNr", plate.getPlateNr());
        model.addAttribute("end", plate.getEnd());

        Optional<Plate> searchResult = plateRepository.findByPlateNr(plate.getPlateNr()).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();

        if (searchResult.isPresent())
            return "plateFound";
        return "plateNotFound";
    }

    @PostMapping("/addPlate")
    public String plateNotFound(@ModelAttribute Plate plate) {
        plate.setEnd(null);
        plate.setStart(Instant.now());
        plateRepository.save(plate);

        return "plateAdded";
    }

    @PostMapping("/stopAndPay")
    public String stopAndPay(@ModelAttribute Plate plate) {
        Optional<Plate> searchResult = plateRepository.findByPlateNr(plate.getPlateNr()).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();
        if (!searchResult.isPresent())
            return "unexpectedError";

        searchResult.get().setEnd(plate.getEnd());

        plateRepository.save(searchResult.get());
        return "successfullyStoppedMeter";
    }
}
