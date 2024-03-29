package org.example.carpooling.controllers.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.*;
import org.example.carpooling.mappers.UserMapper;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.SimpleUserDto;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.example.carpooling.controllers.rest.TravelRestControllerImpl.PAGE_NUMBER;
import static org.example.carpooling.controllers.rest.TravelRestControllerImpl.SIZE_PAGE;


@Tag(name = "User", description = "User REST controller")
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String ERROR_MESSAGE = "You are not authorized";
    public static final String USER_ID_MISMATCH = "User id mismatch";
    public static final String UPDATE_IS_NOT_ALLOWED = "Update is not allowed";
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;


    @Autowired
    public UserRestController(UserService userService, AuthenticationService authenticationService,
                              UserMapper userMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public Page<UserDto> getAllUsers(@RequestHeader HttpHeaders headers,
                                     @RequestParam(defaultValue = PAGE_NUMBER, required = false) int page,
                                     @RequestParam(defaultValue = SIZE_PAGE, required = false) int size,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "username", required = false) String sortBy,
                                     @RequestParam(defaultValue = "asc", required = false) String sortOrder) {

        try {
            User currentUser = authenticationService.tryGetUser(headers);
            if (!currentUser.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            } else {
                TravelFilterOptions filterOptions = new TravelFilterOptions(page, size, keyword, sortBy, sortOrder);
                Page<User> users = userService.getAllUsers(filterOptions, currentUser);
                List<UserDto> anonymizedUsers = users.getContent().stream().map(userMapper::toAnonymizedDto).toList();
                return new PageImpl<>(anonymizedUsers, users.getPageable(), users.getTotalElements());
            }
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationService.tryGetUser(headers);
            User userToGet = userService.getUserById(id, currentUser);
            return userMapper.toAnonymizedDto(userToGet);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

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
            User currentUser = authenticationService.tryGetUser(headers);
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
            User currentUser = authenticationService.tryGetUser(headers);
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
            User currentUser = authenticationService.tryGetUser(headers);
            User userToMakeAdmin = userService.changeUserAdminValue(id, currentUser, true);
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
            User currentUser = authenticationService.tryGetUser(headers);
            User userToUnmakeAdmin = userService.changeUserAdminValue(id, currentUser, false);
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
            User currentUser = authenticationService.tryGetUser(headers);
            User userToBlock = userService.updateUserStatus(id, currentUser, UserStatus.ACTIVE);
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
            User currentUser = authenticationService.tryGetUser(headers);
            User userToUnblock = userService.updateUserStatus(id, currentUser, UserStatus.BLOCKED);
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
            return "Your account is VERIFIED successfully";
        } catch (OperationNotAllowedException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
}
