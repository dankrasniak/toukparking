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

    @Value("${driver.title}") private String titleDriver;
    @Value("${driver.greeting}") private String greeting;
    @Value("${driver.platesText}") private String platesText;

    @Value("${common.submitText}") private String submitText;

    @Value("${return.enteredPlatesText}") private String enteredPlatesText;
    @Value("${return.title}") private String titleReturn;
    @Value("${return.result}") private String result;
    @Value("${return.returnText}") private String returnText;

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("title", titleDriver);
        model.addAttribute("greeting", greeting);
        model.addAttribute("platesText", platesText);
        model.addAttribute("submitText", submitText);
        model.addAttribute("plate", new Plate());
        return "driver";
    }

    @PostMapping("/savePlate")
    public String savePlateSubmit(@ModelAttribute Plate plate, Model model) {
        model.addAttribute("plateNr", plate.getPlateNr());
        model.addAttribute("title", titleReturn);
        model.addAttribute("result", result);
        model.addAttribute("enteredPlatesText", enteredPlatesText);
        model.addAttribute("returnText", returnText);
        return "result";
    }
}
