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
import parking.services.PlateManagerService;
import parking.services.PricingService;

import java.rmi.activation.UnknownObjectException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class DriverController {

    private final PlateManagerService plateManagerService;
    private final PricingService pricingService;
    private final Environment env;

    @Autowired
    public DriverController(PlateManagerService plateManagerService, PricingService pricingService, Environment env) {
        this.plateManagerService = plateManagerService;
        this.pricingService = pricingService;
        this.env = env;
    }

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("plate", new Plate());
        List<Region> regions = Arrays.asList(Region.values());
        model.addAttribute("regions", regions);
        return "driver";
    }

    @PostMapping("/savePlate")
    public String savePlateSubmit(@ModelAttribute Plate plate, Model model) {
        model.addAttribute("plate", plate);

        Optional<Plate> searchResult = plateManagerService.getPlateWithRunningMeter(plate.getPlateNr());

        if (searchResult.isPresent()) {
            plateManagerService.copyStartVipAndStopMeter(plate, searchResult.get());
            try {
                pricingService.updatePrice(plate);
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

        plateManagerService.savePlateWithRunningMeter(Instant.now(), plate);

        return "plateAdded";
    }

    @PostMapping("/stopAndPay")
    public String stopAndPay(@ModelAttribute Plate plate, Model model) {
        Optional<Plate> searchResult = plateManagerService.getPlateWithRunningMeter(plate.getPlateNr());

        if (!searchResult.isPresent()) {
            model.addAttribute("message", env.getProperty("error.plateNotFound"));
            return "unexpectedError";
        }

        plateManagerService.updatePlateWithGivenId(searchResult.get().getId(), plate);
        return "successfullyStoppedMeter";
    }
}
