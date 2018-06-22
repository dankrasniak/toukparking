package parking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;

@Controller
public class DriverController {

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
