package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.mappers.UserMapper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.ProfileDto;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeMvcController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final UserService userService;


    public HomeMvcController(AuthenticationService authenticationService, UserMapper userMapper, UserService userService) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping
    public String showHomePage(Model Model) {
        return "index";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        authenticationService.tryGetCurrentUser(session);
        model.addAttribute("profile", new ProfileDto());
        return "ProfileView";
    }

//    @PostMapping("/profile")
//    public String handleProfileUpdate(@Valid @ModelAttribute("profile") ProfileDto profileDto,
//                                      BindingResult bindingResult, HttpSession session) {
//        User user = authenticationService.tryGetCurrentUser(session);
//        if (bindingResult.hasErrors()) {
//            return "ProfileView";
//        }
//        try {
//            user = userMapper.fromDto(profileDto, user);
//            userService.update(user);
//            return "redirect:/profile";
//        } catch (FirstNameChangeProfileError e) {
//            bindingResult.rejectValue("firstName", "profile_error", e.getMessage());
//            return "ProfileView";
//        } catch (LastNameChangeProfileError e) {
//            bindingResult.rejectValue("lastName", "profile_error", e.getMessage());
//            return "ProfileView";
//        } catch (EmailChangeProfileError e) {
//            bindingResult.rejectValue("email", "profile_error", e.getMessage());
//            return "ProfileView";
//        } catch (PasswordChangeProfileError e) {
//            bindingResult.rejectValue("password", "profile_error", e.getMessage());
//            return "ProfileView";
//        }
//    }


    @GetMapping("/profileDelete")
    public String handleProfileDelete(@Valid @ModelAttribute("profileDelete") ProfileDto profileDto, Model model,
                                      BindingResult bindingResult, HttpSession session) {
        User user = authenticationService.tryGetCurrentUser(session);
        try {
            userService.delete(user.getUserId(), user);
            session.removeAttribute("currentUser");
            return "redirect:/";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        try {
            User user = authenticationService.tryGetCurrentUser(session);
            if (user.isAdmin()) {
                return "AdminPanel";
            }
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

    }
}
