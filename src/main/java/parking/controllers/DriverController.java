package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

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
        model.addAttribute("plateNr", plate.getPlateNr());
        return "result";
    }


}
