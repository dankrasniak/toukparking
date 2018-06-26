package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;
import parking.services.PlateManagerService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class OperatorController {

    private final PlateManagerService plateManagerService;

    @Autowired
    public OperatorController(PlateManagerService plateManagerService) {
        this.plateManagerService = plateManagerService;
    }


    @GetMapping("/operator")
    public String operatorGreeter(Model model) {
        model.addAttribute("plate", new Plate());
        return "operator";
    }

    @PostMapping("/operatorPlateSearch")
    public String operatorPlateSearch(@Valid @ModelAttribute Plate plate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "operator";
        }
        model.addAttribute("plate", plate);
        model.addAttribute("plateNr", plate.getPlateNr());

        Optional<Plate> searchResult = plateManagerService.getPlateWithRunningMeter(plate.getPlateNr());

        if (searchResult.isPresent())
            return "operatorPlateFound";
        return "operatorPlateNotFound";
    }
}
