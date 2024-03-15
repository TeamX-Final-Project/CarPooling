package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.services.contracts.TravelService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping()
    public String showAllTravels(@RequestParam(defaultValue = "0", name = "page") int page,
                                 @RequestParam(defaultValue = "10", name = "size") int size,
                                 @RequestParam(defaultValue = "", name = "keyword") String keyword,
                                 @RequestParam(defaultValue = "startPoint", name = "sortBy") String sortBy,
                                 @RequestParam(defaultValue = "ASC", name = "orderBy") String orderBy,
                                 HttpSession session, Model model) {
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/auth/login";
//        }
        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(page, size, keyword, sortBy, orderBy);

        List<TravelDto> travelDtos = travelService.getAllTravels(travelFilterOptions);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", travelDtos.size() / 10 + 2);
        model.addAttribute("totalItems", travelDtos.size());
        model.addAttribute("travels", travelDtos);
        return "TravelsView";
    }

    @GetMapping("/{id}")
    public String showSingleTravel(Model model, @PathVariable long id) {
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
