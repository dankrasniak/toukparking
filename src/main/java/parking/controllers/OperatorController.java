package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperatorController {

    @Value("${operator.title}") private String title;
    @Value("${operator.greeting}") private String greeting;

    @GetMapping("/operator")
    public String operatorGreeter(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("greeting", greeting);
        return "operator";
    }
}
