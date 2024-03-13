package org.example.carpooling.controllers.rest;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.*;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.UserMapper;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.models.dto.SimpleUserDto;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String ERROR_MESSAGE = "You are not authorized";
    public static final String USER_ID_MISMATCH = "User id mismatch";
    public static final String UPDATE_IS_NOT_ALLOWED = "Update is not allowed";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;


    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper,
                              UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader HttpHeaders headers,
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
            User currentUser = authenticationHelper.tryGetUser(headers);
            if (!currentUser.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            } else {
                UserFilterOptions filterOptions = new UserFilterOptions(phoneNumber, email, username, sortBy, sortOrder);
                List<User> users = userService.getAllUsers(filterOptions);
                return users.stream().map(userMapper::toAnonymizedDto).collect(Collectors.toList());
            }
//                       return userService.getAllUsers(filterOptions);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToGet = userService.getUserById(id, currentUser);
            return userMapper.toAnonymizedDto(userToGet);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

//    @GetMapping("/email:{email}")
//    public User getUserByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            return userService.getByEmail(email, user);
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }

//    @GetMapping("/username:{username}")
//    public User getUserByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            return userService.getByUsername(username, user);
//
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }

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
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            User registeredUser = userService.create(user);
            return userMapper.toAnonymizedDto(registeredUser);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (SendMailException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDto update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            if (userDto.getId() != id) {
                throw new OperationNotAllowedException(USER_ID_MISMATCH);
            }
            User currentUser = authenticationHelper.tryGetUser(headers);
            if (currentUser.getUserId() != id) {
                throw new OperationNotAllowedException(UPDATE_IS_NOT_ALLOWED);
            }
            User userUpdates = userMapper.fromDto(userDto);
            User updatedUser = userService.update(userUpdates);
            return userMapper.toDto(updatedUser);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            userService.delete(id, currentUser);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("{id}/admin")
    public SimpleUserDto makeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToMakeAdmin = userService.makeUserAdmin(id, currentUser);
            return userMapper.toSimpleDto(userToMakeAdmin);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityAttributeAlreadySetException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("{id}/admin/delete")
    public SimpleUserDto unmakeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToUnmakeAdmin = userService.unmakeUserAdmin(id, currentUser);
            return userMapper.toSimpleDto(userToUnmakeAdmin);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/block")
    public SimpleUserDto blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToBlock = userService.blockUser(id, currentUser);
            return userMapper.toSimpleDto(userToBlock);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public SimpleUserDto unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToUnblock = userService.unblockUser(id, currentUser);
            return userMapper.toSimpleDto(userToUnblock);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/{id}/verify")
    public String verify(@PathVariable int id, @RequestParam int securityCode) {
        try {
            userService.verify(id, securityCode);
            return HttpStatus.OK.name();
        } catch (OperationNotAllowedException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PostMapping(value = "/{id}/image")
    public String updateImage(@RequestParam("avatar") MultipartFile file,
                              @RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            if (currentUser.getUserId() == id) {
                throw new AuthorizationException(ERROR_MESSAGE);
            }
            ImageData image = userService.saveImage(file, currentUser);
            return image.getImage();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/{id}/image")
//    public String getImage (@PathVariable long id, @RequestHeader HttpHeaders headers){
//        try {
//            User currentUser = authenticationHelper.tryGetUser(headers);
//            if(currentUser.getUserId() == id) {
//                t
//            }
//        }
//    }
}
