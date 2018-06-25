package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;
import parking.entities.enums.Region;
import parking.repositories.PlateRepository;
import parking.services.Pricing;

import java.rmi.activation.UnknownObjectException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Controller
public class DriverController {

    private final PlateRepository plateRepository;
    private final Pricing pricing;
    private final Environment env;

    @Autowired
    public DriverController(PlateRepository plateRepository, Pricing pricing, Environment env) {
        this.plateRepository = plateRepository;
        this.pricing = pricing;
        this.env = env;
    }

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("plate", new Plate());
        Collection<Region> regions = Arrays.asList(Region.values());
        model.addAttribute("regions", regions);
        return "driver";
    }

    @PostMapping("/savePlate")
    public String savePlateSubmit(@ModelAttribute Plate plate, Model model) {
        plate.setEnd(Instant.now());
        model.addAttribute("plate", plate);

        Optional<Plate> searchResult = plateRepository.findByPlateNr(plate.getPlateNr()).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();

        if (searchResult.isPresent()) {
            plate.setStart(searchResult.get().getStart());
            plate.setVip(searchResult.get().isVip());

            try {
                pricing.updatePrice(plate);
            } catch (UnknownObjectException e) {
                model.addAttribute("message", env.getProperty("error.unknownCurrency") + " " + e.getMessage());
                return "unexpectedError";
            }

            return "plateFound";
        }
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
    public String stopAndPay(@ModelAttribute Plate plate, Model model) {

        Optional<Plate> searchResult = plateRepository.findByPlateNr(plate.getPlateNr()).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();

        if (!searchResult.isPresent()) {
            model.addAttribute("message", env.getProperty("error.plateNotFound"));
            return "unexpectedError";
        }

        searchResult.get().setEnd(plate.getEnd());
        searchResult.get().setRegion(plate.getRegion());
        searchResult.get().setPaid(plate.getPaid());

        plateRepository.save(searchResult.get());
        return "successfullyStoppedMeter";
    }
}
