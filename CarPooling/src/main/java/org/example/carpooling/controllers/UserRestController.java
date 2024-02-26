package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityAlreadyAdminException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.UserMapper;
import org.example.carpooling.models.Dto.UserDto;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String ERROR_MESSAGE = "You are not authorized to receive this information";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String username,
                                  ////TODO double check this since there is no post.id field in User to search from here
//                                  @RequestParam(required = false) Integer postId,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        try {
            UserFilterOptions filterOptions = new UserFilterOptions(firstName, email, username, sortBy, sortOrder);
            return userService.getAllUsers(filterOptions);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        // TODO need to discuss if there is need for only admin to search all users(probably not)
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            ;
////
////            if (!user.isAdmin()) {
////                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
////            }
//            return userService.getAllUsers();
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
    }

    @GetMapping("/{id}")
    public User getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            //TODO probably no need to check if the user is logged in to see only firstName, lastName, username, email
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getById(id);
            //TODO probably no need for Authorization Exception
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

    @GetMapping("/firstName:{firstName}")
    public User getUserByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstName) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByFirstName(firstName, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

//    @GetMapping("/userPosts:{id}")
//    public List<Post> getUserPosts(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User requestingUser = authenticationHelper.tryGetUser(headers);
//            User userToGetPosts = userService.getById(id);
//            return new ArrayList<>(userToGetPosts.getUsersPosts());
//        } catch (EntityNotFoundException e){
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
            User userToBeModified = userMapper.fromDto(id, userDto);

            return userService.update(userToBeModified);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
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

    @PutMapping("/makeAdmin:{id}")
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

    @PutMapping("/unmakeAdmin:{id}")
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

    @PutMapping("/blockUser:{id}")
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

    @PutMapping("/unblockUser:{id}")
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
}
