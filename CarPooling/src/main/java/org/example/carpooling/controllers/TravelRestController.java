package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.OperationNotAllowedException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.TravelMapper;
import org.example.carpooling.models.Candidates;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;

import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.services.contracts.TravelService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {
    public static final String PAGE_NUMBER = "0";
    public static final String SIZE_PAGE = "10";
    private final AuthenticationHelper authenticationHelper;

    private final UserService userService;
    private final TravelService travelService;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelRestController(TravelService travelService,
                                TravelMapper travelMapper,
                                AuthenticationHelper authenticationHelper,
                                UserService userService) {
        this.travelService = travelService;
        this.travelMapper = travelMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TravelDto>> getAllTravels(@RequestParam(defaultValue = PAGE_NUMBER) int page,
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
            List<TravelDto> travelPage = travelService.getAllTravels(travelFilterOptions);
            return ResponseEntity.ok(travelPage);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Travel getTravelById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
//            User user = authenticationHelper.tryGetUser(headers);
            return travelService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public Travel createTravel(@RequestHeader HttpHeaders headers, @Valid @RequestBody TravelDto travelDto) {
        try {
            User creator = authenticationHelper.tryGetUser(headers);
            Travel newTravel = travelMapper.fromDto(travelDto);
            return travelService.create(newTravel, creator);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Travel updateTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable int id,
                               @Valid @RequestBody TravelDto travelDto) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            Travel travelToUpdate = travelMapper.fromDto(id, travelDto);
            return travelService.update(userModifier, travelToUpdate);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/delete:{id}")
    public Travel deleteTravelById(@RequestHeader HttpHeaders headers,
                                   @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return travelService.deleteTravelById(id, userModifier);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/cancel:{id}")
    public Travel cancelTravel(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetUser(headers);
            return travelService.cancel(id, userModifier);
        } catch (AuthorizationException | OperationNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }



//    @PutMapping("/approve:{id}")
//    public Candidates approveTravel(@RequestHeader HttpHeaders headers, @PathVariable int id){
//
//    }
}

