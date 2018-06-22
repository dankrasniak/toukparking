package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${home.title}") private String title;
    @Value("${home.iama}") private String IAmA;
    @Value("${home.driverText}") private String driver_text;
    @Value("${home.operatorText}") private String operator_text;
    @Value("${home.ownerText}") private String owner_text;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("iama", IAmA);
        model.addAttribute("driverText", driver_text);
        model.addAttribute("operatorText", operator_text);
        model.addAttribute("ownerText", owner_text);
        return "index";
    }
}
