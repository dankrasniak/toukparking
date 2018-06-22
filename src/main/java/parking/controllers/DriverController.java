package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;

@Controller
public class DriverController {

    @Value("${greeting.driver}")
    private String greeting;

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("greeting", this.greeting);
        model.addAttribute("plate", new Plate());
        return "driver";
    }

    @PostMapping("/savePlate")
    public String savePlateSubmit(@ModelAttribute Plate plate, Model model) {
        model.addAttribute("plateNr", plate.getPlateNr());
        return "result";
    }
}
