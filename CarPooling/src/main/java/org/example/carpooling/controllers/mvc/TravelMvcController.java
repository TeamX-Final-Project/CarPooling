package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.mappers.TravelMapper;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.SimpleTravelDto;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.models.dto.TravelFilterDto;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.CandidateService;
import org.example.carpooling.services.contracts.TravelService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    private final TravelService travelService;

    private final CandidateService candidateService;
    private final AuthenticationService authenticationService;

    private final TravelMapper travelMapper;

    @Autowired
    public TravelMvcController(TravelService travelService, CandidateService candidateService,
                               AuthenticationService authenticationService,
                               TravelMapper travelMapper) {
        this.travelService = travelService;
        this.candidateService = candidateService;
        this.authenticationService = authenticationService;
        this.travelMapper = travelMapper;
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
        try {
            User user = authenticationService.tryGetCurrentUser(session);
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

            Page<Travel> travelDtos = travelService.getAllTravels(travelFilterOptions);
            List<SimpleTravelDto> simpleTravelDtoList = travelDtos.getContent().stream()
                    .map(travel -> travelMapper.convertToSimpleTravelDto(user, travel)).toList();
            Page<SimpleTravelDto> simpleTravelDtos = new PageImpl<>(simpleTravelDtoList, travelDtos.getPageable(),
                    travelDtos.getTotalElements());

            model.addAttribute("totalPages", travelDtos.getTotalPages());
            model.addAttribute("currentPage", page + 1);
            model.addAttribute("totalItems", travelDtos.getNumberOfElements());
            model.addAttribute("travels", simpleTravelDtos);
            return "TravelsView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/{id}")
    public String showSingleTravel(@PathVariable long id, Model model, HttpSession session) {
        try {
            User user = authenticationService.tryGetCurrentUser(session);
            Travel travel = travelService.getById(id, user);
            List<Candidates> candidatesList = candidateService.checkPendingAndApprovedUsers(user, travel);
            model.addAttribute("travel", travel);
            model.addAttribute("candidates", candidatesList);
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

    @GetMapping("/new")
    public String createTravelView(Model model, HttpSession session) {
        try {
            authenticationService.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("travel", new TravelDto());
        return "CreateTravel";
    }

    @PostMapping("/new")
    public String createTravelView(@Valid @ModelAttribute("travel") TravelDto travelDto,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session) {
        User user;
        try {
            user = authenticationService.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "CreateTravel";
        }
        try {
            Travel travel = travelMapper.fromDto(travelDto);
            travelService.create(travel, user);
            return "redirect:/travels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/update/{id}")
    public String updateTravel(@PathVariable int id, @Valid @ModelAttribute("travelUpdate") TravelDto travelDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session) {
        User user;
        try {
            user = authenticationService.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "UpdateTravelView";
        }
        try {
            Travel travel = travelMapper.fromDto(id, travelDto, user);
            travelService.update(user, travel);
            return "redirect:/travels/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTravel(@PathVariable int id, @Valid @ModelAttribute("travelDelete") Travel travel,
                               Model model,
                               HttpSession session) {
        User user;
        try {
            user = authenticationService.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            travelService.deleteTravelById(id, user);
            return "redirect:/travels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        } catch (OperationNotAllowedException e) {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/cancel/{id}")
    public String cancelTravel(@PathVariable int id, @Valid @ModelAttribute("travelCancel") Travel travel,
                               Model model,
                               HttpSession session) {
        User user;
        try {
            user = authenticationService.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            travelService.cancel(id, user);
            return "redirect:/travels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        } catch (OperationNotAllowedException e) {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
