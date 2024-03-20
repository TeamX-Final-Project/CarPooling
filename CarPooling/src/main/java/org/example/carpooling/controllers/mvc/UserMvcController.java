package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.mappers.UserMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FilterDto;
import org.example.carpooling.models.dto.SimpleUserDto;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.FeedbackService;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            model.addAttribute("currentUser", currentUser);
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


            List<Feedback> feedbacksReceived = feedbackService.getByReceiver(user);
            model.addAttribute("feedbacksReceived", feedbacksReceived);
            List<Travel> listings = travelService.getOpenTravelsOfDriver(user);
            model.addAttribute("listings", listings);

            return "UserView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

//    @GetMapping("/{id}/update")
//    public String showEditUserPage(@PathVariable Long id, Model model, HttpSession session) {
//        try {
//            User currentUser = authenticationService.tryGetCurrentUser(session);
//            model.addAttribute("currentUser", currentUser);
//            User user = userService.getById(id);
//            try {
//                User modifier = authenticationService.tryGetCurrentUser(session);
//                checkSelfModifyPermissions(modifier, user
//                );
//            } catch (EntityNotFoundException e) {
//                model.addAttribute("error", e.getMessage());
//                return "ErrorView";
//            } catch (AuthorizationException e) {
//                model.addAttribute("error", e.getMessage());
//                return "redirect:/auth/login";
//            }
//            UserDto userDto = userMapper.toDtoEdit(user);
//            model.addAttribute("userId", id);
//            model.addAttribute("user", userDto);
//            model.addAttribute("originalUser", user);
//            String profilePictureUrl = user.getProfilePictureUrl();
//            model.addAttribute("profilePictureUrl", profilePictureUrl);
//            return "UpdateProfile";
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
//    @PostMapping("/{id}/update")
//    public String updateUser(@PathVariable Long id,
//                             @Valid @ModelAttribute("user") UserDto userDto,
//                             BindingResult bindingResult,
//                             @RequestParam("profilePicture") MultipartFile profilePicture,
//                             Model model,
//                             HttpSession session) {
//        if (bindingResult.hasErrors()) {
//            return "UpdateProfile";
//        }
//        User modifier;
//        try {
//            modifier = authenticationService.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//        try {
//            User userToUpdate = userMapper.fromDto(id, userDto);
//            User existingUser = userService.getById(id);
//            if (!profilePicture.isEmpty()) {
//                String imageUrl = imageStorageService.uploadImage(profilePicture);
//                userToUpdate.setProfilePictureUrl(imageUrl);
//            }
//            else {
//                userToUpdate.setProfilePictureUrl(existingUser.getProfilePictureUrl());
//            }
//            userService.editUser(modifier, userToUpdate);
//            model.addAttribute("updateSuccess", "Update successful");
//
//            return "UpdateProfile";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        } catch (EntityDuplicateException e) {
//            bindingResult.rejectValue("email", "duplicate_user", e.getMessage());
//            return "UserUpdateView";
//        } catch (AuthorizationException e) {
//            model.addAttribute("error", e.getMessage());
//            return "403";
//        } catch (IOException e) {
//            bindingResult.rejectValue("profilePicture", "image_upload_error", "Image upload failed.");
//            return "UserUpdateView";
//        }
//    }

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

    private void checkSelfModifyPermissions(User modifier, User user) {
        if (!modifier.equals(user)) {
            throw new AuthorizationException("A user can only edit and access his own travel information!");
        }
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("userFilterOptions") FilterDto filterDto, Model model, HttpSession session) {
        try {
        User userModifier = authenticationService.tryGetCurrentUser(session);
        TravelFilterOptions filterOptions = new TravelFilterOptions(
                filterDto.getPage(),
                filterDto.getSize() == 0 ? 10 : filterDto.getSize(),
                filterDto.getKeyword(),
                filterDto.getSortBy() == null ? "username" : filterDto.getSortBy(),
                filterDto.getOrderBy() == null ? "asc" : filterDto.getOrderBy());
        Page<User> usersPage = userService.getAllUsers(filterOptions, userModifier);
        List<SimpleUserDto> simpleUsers = usersPage.getContent().stream().map(userMapper::toSimpleDto).toList();

        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentPage", filterDto.getPage() + 1);
        model.addAttribute("totalItems", usersPage.getNumberOfElements());
        model.addAttribute("users", simpleUsers);
        return "UsersView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/block/{id}")
    public String handleUserBlock(@PathVariable int id, @ModelAttribute("block") User user, Model model,
                                  HttpSession session) {

        try {
            User userModifier = authenticationService.tryGetCurrentUser(session);
            userService.updateUserStatus(id, userModifier, UserStatus.BLOCKED);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/unblock/{id}")
    public String handleUserUnblock(@PathVariable int id, @ModelAttribute("unblock") User user, Model model,
                                    HttpSession session) {

        try {
            User userModifier = authenticationService.tryGetCurrentUser(session);
            userService.updateUserStatus(id, userModifier, UserStatus.ACTIVE);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/makeAdmin/{id}")
    public String handleUserMakeAdmin(@PathVariable int id, @ModelAttribute("makeAdmin") User user, Model model,
                                      HttpSession session) {
        try {
            User userModifier = authenticationService.tryGetCurrentUser(session);
            userService.changeUserAdminValue(id, userModifier, true);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/unmakeAdmin/{id}")
    public String handleUserUnMakeAdmin(@PathVariable int id, @ModelAttribute("unmakeAdmin") User user, Model model,
                                        HttpSession session) {
        try {
            User userModifier = authenticationService.tryGetCurrentUser(session);
            userService.changeUserAdminValue(id, userModifier, false);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

    @GetMapping("/delete/{id}")
    public String handleUserDelete(@PathVariable int id, @ModelAttribute("delete") User user, Model model,
                                   HttpSession session) {
        try {
            User userModifier = authenticationService.tryGetCurrentUser(session);
            userService.delete(id, userModifier);
            return "redirect:/users";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", "Not authorized");
            return "ErrorView";
        }
    }

}
