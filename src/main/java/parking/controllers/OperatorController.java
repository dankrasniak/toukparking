package parking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperatorController {

    @GetMapping("/operator")
    public String operatorGreeter(Model model) {
        return "operator";
    }
}
