package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.BlockedUserException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.TravelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.dto.TravelDto;
import org.example.carpooling.services.contracts.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {

    private final TravelService travelService;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelRestController(TravelService travelService, TravelMapper travelMapper) {
        this.travelService = travelService;
        this.travelMapper = travelMapper;
    }

    @GetMapping
    public List<Travel> getAllTravels(@RequestParam(required = false) String title,
                                      @RequestParam(required = false) String startPoint,
                                      @RequestParam(required = false) String endPoint,
                                      @RequestParam(required = false) String createdBy,
                                      @RequestParam(required = false) String sortBy,
                                      @RequestParam(required = false) String orderBy) {
        try {
            TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                    title,
                    startPoint,
                    endPoint,
                    createdBy,
                    sortBy,
                    orderBy);
            return travelService.getAllTravels(travelFilterOptions);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Travel getTravelById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            //TODO wait for the User class implementation for the logic to authenticate the user before searching for travel by id
            User user = authenticationHelper.tryGetCurrentUser(headers);
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
            //TODO wait for the User class implementation for the logic to authenticate the user before creating a new travel
            User creator = authenticationHelper.tryGetCurrentUser(headers);
            Travel newTravel = travelMapper.fromDto(travelDto);
            return travelService.create(newTravel, creator);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Travel updateTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable int id,
                               @Valid @RequestBody TravelDto travelDto) {
        try {
            User userModifier = authenticationHelper.tryGetCurrentUser(headers);
            Travel travelToUpdate = travelMapper.fromDto(id, travelDto);
            return travelService.update(userModifier, travelToUpdate);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/delete:{id}")
    public Travel deleteTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable int id) {
        try {
            User userModifier = authenticationHelper.tryGetCurrentUser(headers);
            return travelService.delete(id, userModifier);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/cancel:{id}")
    public Travel cancelTravel(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User userModifier = authenticationHelper.tryGetCurrentUser(headers);
            return travelService.cancel(id, userModifier);
        } catch (AuthorizationException | BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

