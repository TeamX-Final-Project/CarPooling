package org.example.carpooling.controllers.mvc;

import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    private final TravelService travelService;

    @Autowired
    public TravelMvcController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public String showAllTravels(
            @ModelAttribute("travelFilterOptions") TravelFilterOptions travelFilterOptions,
            Model model) {
        model.addAttribute("travels", travelService.getAllTravels(travelFilterOptions));
        return "TravelsView";
    }
}
