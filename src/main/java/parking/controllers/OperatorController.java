package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import java.util.Optional;

@Controller
public class OperatorController {

    private final PlateRepository plateRepository;

    @Autowired
    public OperatorController(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }


    @GetMapping("/operator")
    public String operatorGreeter(Model model) {
        model.addAttribute("plate", new Plate());
        return "operator";
    }

    @PostMapping("/operatorPlateSearch")
    public String operatorPlateSearch(@ModelAttribute Plate plate, Model model) {
        model.addAttribute("plate", plate);
        model.addAttribute("plateNr", plate.getPlateNr());

        Optional<Plate> searchResult = plateRepository.findByPlateNr(plate.getPlateNr()).parallelStream()
                .filter(p -> p.getEnd() == null).findAny();

        if (searchResult.isPresent())
            return "operatorPlateFound";
        return "operatorPlateNotFound";
    }
}
