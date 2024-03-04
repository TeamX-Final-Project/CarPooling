package org.example.carpooling.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.*;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.UserMapper;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.dto.ProfilePicture;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String ERROR_MESSAGE = "You are not authorized to receive this information";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final Cloudinary cloudinary;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper,
                              UserMapper userMapper, Cloudinary cloudinary) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.cloudinary = cloudinary;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
//        try {
//            UserFilterOptions filterOptions = new UserFilterOptions(phoneNumber, email, username, sortBy, sortOrder);
//            return userService.getAllUsers(filterOptions);
//        } catch (EntityNotFoundException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            } else {
                UserFilterOptions filterOptions = new UserFilterOptions(phoneNumber, email, username, sortBy, sortOrder);
                return userService.getAllUsers(filterOptions);
            }
//                       return userService.getAllUsers(filterOptions);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            //todo Pet: make access check
            //todo Pet:  (anonymous user) not logged in user should NOT be abel to see any user private data
            //todo Pet: only admin can access other user data ;
            return userService.getById(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/email:{email}")
    public User getUserByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByEmail(email, user);
            //TODO check if need to catch authorization exception
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/username:{username}")
    public User getUserByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByUsername(username, user);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

//    @GetMapping("/firstName:{firstName}")
//    public User getUserByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstName) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            return userService.getByFirstName(firstName, user);
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }


    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            return userService.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            if (userModifier.getUserId() != id) {
                throw new OperationNotAllowedException("Update is not allowed");
            }
            User userUpdates = userMapper.fromDto(id, userDto);
            return userService.update(userUpdates);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public User delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return userService.delete(id, userModifier);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("{id}/admin")
    public User makeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return userService.makeUserAdmin(id, userModifier);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAlreadyAdminException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("{id}/admin/delete")
    public User unmakeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return userService.unmakeUserAdmin(id, userModifier);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/block")
    public User blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return userService.blockUser(id, userModifier);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public User unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return userService.unblockUser(id, userModifier);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
//    @PostMapping(value = "/updateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/image")
//    public String updateImage(@RequestParam ProfilePicture picture, HttpHeaders headers) {
    public String updateImage(@RequestParam("avatar") MultipartFile file, @RequestHeader HttpHeaders headers) {
        try {

            User user = authenticationHelper.tryGetUser(headers);
            String url = cloudinaryUploader(file);
            ImageData imageData = new ImageData();
            imageData.setImage(url);
            imageData.setUser(user);
            userService.saveImage(imageData, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/user";
    }
    private String cloudinaryUploader(MultipartFile file) throws IOException {
        Map upload = cloudinary.uploader()
                .upload(file.getBytes()
                        , ObjectUtils.asMap("resource_type", "auto"));

        return (String) upload.get("url");
    }
}
