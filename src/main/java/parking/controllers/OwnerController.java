package parking.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OwnerController {

    @Value("${owner.title}") private String title;
    @Value("${owner.greeting}") private String greeting;

    @GetMapping("/owner")
    public String ownerGreeter(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("greeting", greeting);
        return "owner";
    }

}