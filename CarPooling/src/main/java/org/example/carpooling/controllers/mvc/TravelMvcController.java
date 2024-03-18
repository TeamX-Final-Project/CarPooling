package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.models.dto.TravelFilterDto;
import org.example.carpooling.services.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    @Autowired
    public TravelMvcController(TravelService travelService, AuthenticationService authenticationService) {
        this.travelService = travelService;
        this.authenticationService = authenticationService;
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
    public String showAllTravels(@RequestParam(defaultValue = "0", name = "page") int page,
                                 @RequestParam(defaultValue = "10", name = "size") int size,
                                 @RequestParam(defaultValue = "", required = false, name = "keyword") String keyword,
                                 @RequestParam(defaultValue = "startPoint", required = false, name = "sortBy") String sortBy,
                                 @RequestParam(defaultValue = "asc", name = "orderBy") String orderBy,
                                 @ModelAttribute("travelFilterOptions") TravelFilterDto travelFilterDto,
                                 HttpSession session,
                                 Model model) {

        if (!populateIsAuthenticated(session)) {
            return "redirect:/auth/login";
        }
        travelFilterDto.setPage(page);
        travelFilterDto.setSize(size);
        travelFilterDto.setKeyword(keyword);
        travelFilterDto.setSortBy(sortBy);
        travelFilterDto.setOrderBy(orderBy);

        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                travelFilterDto.getPage(),
                travelFilterDto.getSize(),
                travelFilterDto.getKeyword(),
                travelFilterDto.getSortBy(),
                travelFilterDto.getOrderBy());

        Page<TravelDto> travelDtos = travelService.getAllTravels(travelFilterOptions);


        model.addAttribute("totalPages", travelDtos.getTotalPages());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalItems", travelDtos.getNumberOfElements());
        model.addAttribute("travels", travelDtos);
        return "TravelsView";
    }

    @GetMapping("/{id}")
    public String showSingleTravel(@PathVariable long id, Model model, HttpSession session) {
        User user;
        Travel travel = travelService.getById(id);
        try {
            user = authenticationService.tryGetCurrentUser(session);
            model.addAttribute("travel", travel);
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
