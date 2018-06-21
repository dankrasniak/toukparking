package parking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DriverController {

    @GetMapping("/driver")
    public String greeting() {
        return "/driver";
    }
}
