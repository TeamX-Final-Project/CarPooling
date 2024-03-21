package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.exceptions.TravelException;
import org.example.carpooling.mappers.FeedbackMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FeedbackDto;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.FeedbackService;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackMvcController {
    private final AuthenticationService authenticationService;
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final TravelService travelService;
    private final UserService userService;

    @Autowired
    public FeedbackMvcController(AuthenticationService authenticationService, FeedbackService feedbackService, FeedbackMapper feedbackMapper, TravelService travelService, UserService userService) {
        this.authenticationService = authenticationService;
        this.feedbackService = feedbackService;
        this.feedbackMapper = feedbackMapper;
        this.travelService = travelService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(HttpSession httpSession) {
        try {
            return authenticationService.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return null;
        }
    }

    @GetMapping("/new/travel:{travelId}/user:{receiverUserId}")
    public String showNewFeedbackPage(@PathVariable("travelId") Long travelId,
                                      @PathVariable("receiverUserId") Long receiverUserId,
                                      Model model, HttpSession httpSession) {
        try {
            User currentUser = authenticationService.tryGetCurrentUser(httpSession);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("travelId", travelId);
            model.addAttribute("receiverUserId", receiverUserId);

            if (currentUser == null) {
                return "redirect:/auth/login";
            }
            model.addAttribute("feedback", new FeedbackDto());
            model.addAttribute("travelId", travelId);
            model.addAttribute("receiverUserId", receiverUserId);
            model.addAttribute("travel", travelService.getById(travelId, currentUser));
            model.addAttribute("receiver", userService.getById(receiverUserId));
            return "AddFeedback";
        } catch (AuthorizationException e) {
            return "ErrorView";
        }
    }

    @PostMapping("/new/travel:{travelId}/user:{receiverUserId}")
    public String createFeedback(@ModelAttribute("feedback") FeedbackDto feedbackDto,
                                 @PathVariable("travelId") Long travelId,
                                 @PathVariable("receiverUserId") Long receiverUserId,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        if (errors.hasErrors()) {
            return "AddFeedback";
        }
        try {
            User currentUser = authenticationService.tryGetCurrentUser(session);
            if (currentUser == null) {
                return "redirect:/auth/login";
            }
            Travel travel = travelService.getById(travelId, currentUser);
            User receiverUser = userService.getById(receiverUserId);
            Feedback feedback = feedbackMapper.fromFeedbackDto(feedbackDto, currentUser, receiverUser, travel);
            feedbackService.create(feedback);
            return "redirect:/travels/" + travelId;

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (TravelException e) {
            return "AddFeedback";
        } catch (OperationNotAllowedException e) {
            return "redirect:/travels/" + travelId;
        }
    }
}




