package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DriverController {

    @Value("${greeting.driver}")
    private String greeting;

    @GetMapping("/driver")
    public String driverGreeter(Model model) {
        model.addAttribute("greeting", this.greeting);
        return "/driver";
    }
}
