package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.ImageHelper;
import org.example.carpooling.mappers.UserMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.models.dto.UserFilterDto;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.FeedbackService;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final TravelService travelService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final FeedbackService feedbackService;


    @Autowired
    public UserMvcController(UserService userService, TravelService travelService, UserMapper userMapper, AuthenticationService authenticationService, FeedbackService feedbackService) {
        this.userService = userService;
        this.travelService = travelService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
        this.feedbackService = feedbackService;
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
    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable Long id, Model model, HttpSession httpSession) {
        try {
            User currentUser = authenticationService.tryGetCurrentUser(httpSession);
            model.addAttribute("currentUser",currentUser);
            model.addAttribute("userService", userService);
            model.addAttribute("feedbackService", feedbackService);
            User user = userService.getById(id);

            int completedTravelsAsDriverCount = travelService.getCompletedTravelsAsDriverCount(user);
            int completedTravelsAsPassengerCount = travelService.getCompletedTravelsAsPassengerCount(user);

            model.addAttribute("user", user);
            model.addAttribute("id", id);

            String profilePictureUrl = user.getProfilePictureUrl();
            model.addAttribute("profilePictureUrl", profilePictureUrl);
            model.addAttribute("completedCountAsDriver", completedTravelsAsDriverCount);
            model.addAttribute("completedCountAsPassenger", completedTravelsAsPassengerCount);


            List<Feedback> feedbacksReceived= feedbackService.getByReceiver(user);
            model.addAttribute("feedbacksReceived",feedbacksReceived);
            List<Travel> listings = travelService.getOpenTravelsOfDriver(user);
            model.addAttribute("listings",listings);

            return "UserView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @PostMapping("/picture")
    public String addProfilePhoto(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
        try {
            User user = authenticationService.tryGetCurrentUser(httpSession);
            userService.addProfilePhoto(user, file);
            return "redirect:/users/update";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            return "ErrorView";
        }
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto, Model model) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterDto.getFirstName(),
                userFilterDto.getUsername(), userFilterDto.getEmail(),
                userFilterDto.getSortBy(), userFilterDto.getOrderBy());
        model.addAttribute("users", userService.getAllUsers(userFilterOptions));
        return "UsersView";
    }
//    @PostMapping("/block:{id}")
//    public String handleUserBlock(@PathVariable int id, @ModelAttribute("block") User user, Model model,
//                                  HttpSession session) {
//        User userModifier = authenticationService.tryGetCurrentUser(session);
//        try {
//            userService.updateUserStatus(id, userModifier);
//            return "redirect:/users";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", "Not authorized");
//            return "ErrorView";
//        }
//    }
//
//    @PostMapping("/unblock:{id}")
//    public String handleUserUnblock(@PathVariable int id, @ModelAttribute("unblock") User user, Model model,
//                                    HttpSession session) {
//        User userModifier = authenticationService.tryGetCurrentUser(session);
//        try {
//            userService.updateUserStatus(id, userModifier);
//            return "redirect:/users";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", "Not authorized");
//            return "ErrorView";
//        }
//    }
//
//    @PostMapping("/makeAdmin:{id}")
//    public String handleUserMakeAdmin(@PathVariable int id, @ModelAttribute("makeAdmin") User user, Model model,
//                                      HttpSession session) {
//        User userModifier = authenticationService.tryGetCurrentUser(session);
//        try {
//            userService.changeUserAdminValue(id, userModifier);
//            return "redirect:/users";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", "Not authorized");
//            return "ErrorView";
//        }
//    }
//
//    @PostMapping("/unmakeAdmin:{id}")
//    public String handleUserUnMakeAdmin(@PathVariable int id, @ModelAttribute("unmakeAdmin") User user, Model model,
//                                        HttpSession session) {
//        User userModifier = authenticationService.tryGetCurrentUser(session);
//        try {
//            userService.changeUserAdminValue(id, userModifier);
//            return "redirect:/users";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", "Not authorized");
//            return "ErrorView";
//        }
//    }

    @PostMapping("/delete:{id}")
    public String handleUserDelete(@PathVariable int id, @ModelAttribute("delete") User user, Model model,
                                   HttpSession session) {
        User userModifier = authenticationService.tryGetCurrentUser(session);
        try {
            userService.delete(id, userModifier);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

}
