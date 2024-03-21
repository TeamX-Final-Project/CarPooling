package org.example.carpooling.controllers.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.carpooling.controllers.rest.contracts.TravelRestController;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.mappers.TravelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.services.AuthenticationService;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Travel", description = "Travel REST controller")
@RestController
@RequestMapping("/api/travels")
public class TravelRestControllerImpl implements TravelRestController {
    public static final String PAGE_NUMBER = "0";
    public static final String SIZE_PAGE = "10";
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final TravelService travelService;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelRestControllerImpl(TravelService travelService,
                                    TravelMapper travelMapper,
                                    AuthenticationService authenticationService,
                                    UserService userService) {
        this.travelService = travelService;
        this.travelMapper = travelMapper;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @Override
    @GetMapping
    public List<TravelDto> getAllTravels(@RequestParam(defaultValue = PAGE_NUMBER) int page,
                                         @RequestParam(defaultValue = SIZE_PAGE) int size,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(defaultValue = "ASC") String orderBy) {
        try {
            TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                    page,
                    size,
                    keyword,
                    sortBy,
                    orderBy);

            return travelService.getAllTravels(travelFilterOptions).stream()
                    .map(travelMapper::convertToDto)
                    .toList();

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @GetMapping("/{id}")
    public Travel getTravelById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationService.tryGetUser(headers);
            return travelService.getById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    @PostMapping
    public Travel createTravel(@RequestHeader HttpHeaders headers, @Valid @RequestBody TravelDto travelDto) {
        try {
            User creator = authenticationService.tryGetUser(headers);
            Travel newTravel = travelMapper.fromDto(travelDto);
            return travelService.create(newTravel, creator);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
    }

    @Override
    @PutMapping("/{id}")
    public Travel updateTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable long id,
                               @Valid @RequestBody TravelDto travelDto) {
        try {
            User userModifier = authenticationService.tryGetUser(headers);
            Travel travelToUpdate = travelMapper.fromDto(id, travelDto, userModifier);
            return travelService.update(userModifier, travelToUpdate);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @PutMapping("/delete:{id}")
    public Travel deleteTravelById(@RequestHeader HttpHeaders headers,
                                   @PathVariable long id) {
        try {
            User userModifier = authenticationService.tryGetUser(headers);
            return travelService.deleteTravelById(id, userModifier);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @PutMapping("/cancel:{id}")
    public Travel cancelTravel(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User userModifier = authenticationService.tryGetUser(headers);
            return travelService.cancel(id, userModifier);
        } catch (AuthorizationException | OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

