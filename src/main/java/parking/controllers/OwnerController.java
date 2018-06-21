package parking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OwnerController {

    @GetMapping("/owner")
    public String ownerGreeter() {
        return "/owner";
    }

}