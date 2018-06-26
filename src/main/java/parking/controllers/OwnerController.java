package parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import parking.entities.DateContainer;
import parking.services.PricingService;

import java.time.LocalDate;


@Controller
public class OwnerController {

    private final PricingService pricingService;

    @Autowired
    public OwnerController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/owner")
    public String ownerGreeter(Model model) {
        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        model.addAttribute("dateContainer", dateContainer);
        return "owner";
    }

    @PostMapping("/getIncome")
    public String getIncome(@ModelAttribute DateContainer dateContainer, Model model) {
        model.addAttribute("date", dateContainer.getDateTime().toString());
        model.addAttribute("income", pricingService.getIncomeForDay(dateContainer));
        return "income";
    }
}