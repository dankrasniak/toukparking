package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperatorController {

    @Value("${greeting.operator}")
    private String greeting;

    @GetMapping("/operator")
    public String operatorGreeter(Model model) {
        model.addAttribute("greeting", this.greeting);
        return "/operator";
    }
}
