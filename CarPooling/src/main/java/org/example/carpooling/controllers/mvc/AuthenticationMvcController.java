package org.example.carpooling.controllers.mvc;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.InvalidPasswordException;
import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.mappers.UserMapper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.LoginDto;
import org.example.carpooling.models.dto.RegisterDto;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {
    public static final String PASSWORD_CONFIRM_NEED_TO_MATCH_WITH_PASSWORD_ERROR = "Password confirm need to match with password";
    public static final String FAILED_TO_SEND_VALIDATION_MAIL_ERROR = "System failed to send validation mail. Please, register again.";
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public AuthenticationMvcController(UserService userService,
                                       AuthenticationService authenticationService,
                                       UserMapper userMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }
        try {
            User user = authenticationService.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            if (!UserStatus.ACTIVE.equals(user.getUserStatus())) {
                return "redirect:/";
            } else {
                session.setAttribute("currentUser", user.getUsername());
                session.setAttribute("profilePictureUrl", user.getProfilePictureUrl());
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("isAdmin", user.isAdmin());
                session.setAttribute("isBlocked", user.getUserStatus());
                session.setAttribute("isDeleted", user.getUserStatus());
                return "redirect:/users/" + user.getUserId();
            }
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            bindingResult.rejectValue("password", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.removeAttribute("profilePictureUrl");
        session.removeAttribute("userId");
        session.removeAttribute("isAdmin");
        session.removeAttribute("isBlocked");
        session.removeAttribute("isDeleted");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }
        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            bindingResult.rejectValue("password",
                    "password_error",
                    PASSWORD_CONFIRM_NEED_TO_MATCH_WITH_PASSWORD_ERROR);
            return "RegisterView";
        }

        try {
            User user = userMapper.fromDto(registerDto);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (SendMailException e) {
            bindingResult.rejectValue("email",
                    "email_error",
                    FAILED_TO_SEND_VALIDATION_MAIL_ERROR);
            return "RegisterView";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password",
                    "password_error",
                    e.getMessage());
            return "RegisterView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue(e.getAttribute(),
                    e.getAttribute() + "_error",
                    e.getMessage());
            return "RegisterView";
        }
    }
}
