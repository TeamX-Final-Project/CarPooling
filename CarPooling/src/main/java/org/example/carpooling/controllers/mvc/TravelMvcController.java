package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
=======
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.services.contracts.TravelService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    private final TravelService travelService;

    @Autowired
    public TravelMvcController(TravelService travelService) {
        this.travelService = travelService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {

        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String getRequestURI(HttpServletRequest request) {

        return request.getRequestURI();
    }

    @GetMapping
    public String showAllTravels(
            @ModelAttribute("travelFilterOptions") TravelFilterOptions travelFilterOptions,
            Model model) {
        model.addAttribute("travels", travelService.getAllTravels(travelFilterOptions));
        return "TravelsView";
    }

    @GetMapping("/{id}")
    public String showSingleTravel(Model model, @PathVariable long id){
        try {
            model.addAttribute("travel", travelService.getById(id));
            return "TravelView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

    }
}
