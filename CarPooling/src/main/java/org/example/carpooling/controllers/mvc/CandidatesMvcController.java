package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.mappers.TravelMapper;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.User;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.CandidateService;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidates")
public class CandidatesMvcController {
    private final TravelService travelService;
    private final CandidateService candidateService;
    private final AuthenticationService authenticationService;

    private final TravelMapper travelMapper;

    @Autowired
    public CandidatesMvcController(TravelService travelService, CandidateService candidateService,
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

    @PostMapping("/apply/{id}")
    public String applyTravel(@PathVariable int id,
                              Model model,
                              HttpSession session) {
        try {
            User user = authenticationService.tryGetCurrentUser(session);
            candidateService.applyTravel(id, user);
            model.addAttribute("currentUser", user);
            return "redirect:/travels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException | BlockedUserException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        } catch (OperationNotAllowedException e) {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/approve/{candidateId}/travel/{travelId}")
    public String approveTravel(@PathVariable long candidateId,
                                @PathVariable long travelId,
                                Model model,
                                HttpSession session) {
        User userToConfirmApprove;
        Candidates userToApprove;
        try {
            userToConfirmApprove = authenticationService.tryGetCurrentUser(session);
            userToApprove = candidateService.findById(candidateId);
            model.addAttribute("candidate", userToApprove);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            candidateService.approveTravel(travelId, userToConfirmApprove, userToApprove);
            return "redirect:/travels/" + travelId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException | BlockedUserException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        } catch (OperationNotAllowedException e) {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/reject/{candidateId}/travel/{travelId}")
    public String rejectTravel(@PathVariable long candidateId,
                               @PathVariable long travelId,
                               Model model,
                               HttpSession session) {
        User userToConfirmReject;
        Candidates userToReject;
        try {
            userToConfirmReject = authenticationService.tryGetCurrentUser(session);
            userToReject = candidateService.findById(candidateId);
            model.addAttribute("candidate", userToReject);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            candidateService.rejectTravel(travelId, userToConfirmReject, userToReject);
            return "redirect:/travels/" + travelId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException | BlockedUserException e) {
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
